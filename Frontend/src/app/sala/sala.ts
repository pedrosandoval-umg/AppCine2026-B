import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-sala',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sala.html',
  styleUrl: './sala.css'
})
export class Sala implements OnInit {

  salas: any[] = [];

  sala = {
    idSala: null,
    nombre: '',
    filas: null,
    asientosPorFila: null,
    estado: true
  };

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.buscarSalas();
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
          alert('Error al obtener salas.');
        }
      });
  }

  guardarSala(): void {

    if (
      this.sala.nombre.trim() === '' ||
      this.sala.filas == null ||
      this.sala.asientosPorFila == null
    ) {
      alert('Complete todos los datos.');
      return;
    }

    this.http
      .post<any>(
        'http://localhost:8080/sala/guardar',
        this.sala
      )
      .subscribe({
        next: () => {
          alert('Sala guardada exitosamente');
          this.limpiar();
          this.buscarSalas();
        },
        error: (error) => {
          console.error('Error al guardar sala:', error);
          alert('Error al guardar sala.');
        }
      });
  }

  eliminarSala(idSala: number): void {
    this.http
      .delete(`http://localhost:8080/sala/eliminar/${idSala}`)
      .subscribe({
        next: () => {
          alert('Sala eliminada exitosamente');
          this.buscarSalas();
        },
        error: (error) => {
          console.error('Error al eliminar sala:', error);
          alert('Error al eliminar sala.');
        }
      });
  }

  limpiar(): void {
    this.sala = {
      idSala: null,
      nombre: '',
      filas: null,
      asientosPorFila: null,
      estado: true
    };
  }

  configurarAsientos(idSala: number): void {
  location.href = "/asientos/" + idSala;
}


}