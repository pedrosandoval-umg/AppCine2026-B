import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-peliculas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './peliculas.html',
  styleUrl: './peliculas.css',
})
export class Peliculas implements OnInit {
  peliculas: any[] = [];
  generos: any[] = [];
  clasificaciones: any[] = [];

  pelicula = {
    idPelicula: null as number | null,
    titulo: '',
    sinopsis: '',
    duracionMinutos: null as number | null,
    idGenero: null as number | null,
    idClasificacion: null as number | null,
    imagenPromocional: '',
    estado: true,
  };

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.buscarPeliculas();
    this.buscarGeneros();
    this.buscarClasificaciones();
  }

  buscarPeliculas(): void {
    this.http.get<any[]>('http://localhost:8080/pelicula/buscar').subscribe({
      next: (data) => {
        this.peliculas = data;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Error al obtener películas:', error);
        alert('Error al obtener películas.');
      },
    });
  }

  buscarGeneros(): void {
    this.http.get<any[]>('http://localhost:8080/genero/buscar').subscribe({
      next: (data) => {
        this.generos = data;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Error al obtener géneros:', error);
      },
    });
  }

  buscarClasificaciones(): void {
    this.http.get<any[]>('http://localhost:8080/clasificacion/buscar').subscribe({
      next: (data) => {
        this.clasificaciones = data;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Error al obtener clasificaciones:', error);
      },
    });
  }

  guardarPelicula(): void {
    if (
      this.pelicula.titulo.trim() === '' ||
      this.pelicula.sinopsis.trim() === '' ||
      this.pelicula.duracionMinutos === null ||
      this.pelicula.duracionMinutos <= 0 ||
      this.pelicula.idGenero === null ||
      this.pelicula.idClasificacion === null
    ) {
      alert('Complete todos los campos correctamente.');
      return;
    }

    this.http.post<any>('http://localhost:8080/pelicula/guardar', this.pelicula).subscribe({
      next: () => {
        alert('Película guardada exitosamente.');
        this.limpiar();
        this.buscarPeliculas();
      },
      error: (error) => {
        console.error('Error al guardar película:', error);
        alert('Error al guardar película.');
      },
    });
  }

  editarPelicula(item: any): void {
    this.pelicula = {
      idPelicula: item.idPelicula,
      titulo: item.titulo,
      sinopsis: item.sinopsis,
      duracionMinutos: item.duracionMinutos,
      idGenero: item.idGenero,
      idClasificacion: item.idClasificacion,
      imagenPromocional: item.imagenPromocional,
      estado: item.estado,
    };
  }

  eliminarPelicula(idPelicula: number): void {
    if (!confirm('¿Desea eliminar esta película?')) {
      return;
    }

    this.http.delete(`http://localhost:8080/pelicula/eliminar/${idPelicula}`).subscribe({
      next: () => {
        alert('Película eliminada exitosamente.');
        this.buscarPeliculas();
      },
      error: (error) => {
        console.error('Error al eliminar película:', error);
        alert('Error al eliminar película.');
      },
    });
  }

  obtenerGenero(idGenero: number): string {
    const genero = this.generos.find((item) => item.idGenero === idGenero);
    return genero ? genero.nombre : '';
  }

  obtenerClasificacion(idClasificacion: number): string {
    const clasificacion = this.clasificaciones.find(
      (item) => item.idClasificacion === idClasificacion,
    );
    return clasificacion ? clasificacion.nombre : '';
  }

  limpiar(): void {
    this.pelicula = {
      idPelicula: null,
      titulo: '',
      sinopsis: '',
      duracionMinutos: null,
      idGenero: null,
      idClasificacion: null,
      imagenPromocional: '',
      estado: true,
    };
    this.cdr.markForCheck();
  }

  seleccionarImagenPromocional(event: Event) {
    const newfile = event.target as HTMLInputElement;

    if (newfile.files && newfile.files.length > 0) {
      const archivo = newfile.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        const base64 = reader.result as string;

        this.pelicula.imagenPromocional = base64;
        this.cdr.markForCheck();
      };

      reader.readAsDataURL(archivo);
    }
  }
}
