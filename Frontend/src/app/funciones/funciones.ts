
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-funciones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './funciones.html',
  styleUrl: './funciones.css'
})
export class Funciones implements OnInit {

  funciones: any[] = [];
  peliculas: any[] = [];
  salas: any[] = [];

  funcion = {
    idFuncion: null as number | null,
    idPelicula: null as number | null,
    idSala: null as number | null,
    fecha: '',
    hora: '',
    precioEntrada: null as number | null,
    estado: true
  };

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.buscarFunciones();
    this.buscarPeliculas();
    this.buscarSalas();
  }

  buscarFunciones(): void {
    this.http
      .get<any[]>('http://localhost:8080/funcion/buscar')
      .subscribe({
        next: (data) => {
          this.funciones = data;
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Error al obtener funciones:', error);
          alert('Error al obtener funciones.');
        }
      });
  }

  buscarPeliculas(): void {
    this.http
      .get<any[]>('http://localhost:8080/pelicula/buscar')
      .subscribe({
        next: (data) => {
          this.peliculas = data;
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Error al obtener películas:', error);
        }
      });
  }

  buscarSalas(): void {
    this.http
      .get<any[]>('http://localhost:8080/sala/buscar')
      .subscribe({
        next: (data) => {
          this.salas = data;
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Error al obtener salas:', error);
        }
      });
  }

  guardarFuncion(): void {

    if (
      this.funcion.idPelicula === null ||
      this.funcion.idSala === null ||
      this.funcion.fecha === '' ||
      this.funcion.hora === '' ||
      this.funcion.precioEntrada === null ||
      this.funcion.precioEntrada <= 0
    ) {
      alert('Complete todos los campos correctamente.');
      return;
    }

    this.http
      .post<any>(
        'http://localhost:8080/funcion/guardar',
        this.funcion
      )
      .subscribe({
        next: () => {
          alert('Función guardada exitosamente.');
          this.limpiar();
          this.buscarFunciones();
        },
        error: (error) => {
          console.error('Error al guardar función:', error);
          alert('Error al guardar función.');
        }
      });
  }

  editarFuncion(item: any): void {
    this.funcion = {
      idFuncion: item.idFuncion,
      idPelicula: item.idPelicula,
      idSala: item.idSala,
      fecha: item.fecha,
      hora: item.hora ? item.hora.substring(0, 5) : '',
      precioEntrada: item.precioEntrada,
      estado: item.estado
    };

    this.cdr.markForCheck();
  }

  eliminarFuncion(idFuncion: number): void {

    if (!confirm('¿Desea eliminar esta función?')) {
      return;
    }

    this.http
      .delete(
        `http://localhost:8080/funcion/eliminar/${idFuncion}`
      )
      .subscribe({
        next: () => {
          alert('Función eliminada exitosamente.');
          this.buscarFunciones();
        },
        error: (error) => {
          console.error('Error al eliminar función:', error);
          alert('Error al eliminar función.');
        }
      });
  }

  obtenerPelicula(idPelicula: number): string {
    const pelicula = this.peliculas.find(
      item => item.idPelicula === idPelicula
    );

    return pelicula ? pelicula.titulo : '';
  }

  obtenerSala(idSala: number): string {
    const sala = this.salas.find(
      item => item.idSala === idSala
    );

    return sala ? sala.nombre : '';
  }

  limpiar(): void {
    this.funcion = {
      idFuncion: null,
      idPelicula: null,
      idSala: null,
      fecha: '',
      hora: '',
      precioEntrada: null,
      estado: true
    };

    this.cdr.markForCheck();
  }
}
