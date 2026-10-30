import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-genero',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './genero.html',
  styleUrl: './genero.css'
})
export class Genero implements OnInit {

  generos: any[] = [];

  genero = {
    idGenero: null,
    nombre: ''
  };

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.buscarGeneros();
  }

  buscarGeneros(): void {
    this.http
      .get<any[]>('http://localhost:8080/genero/buscar')
      .subscribe({
        next: (data) => {
          this.generos = data;
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Error al obtener géneros:', error);
          alert('Error al obtener géneros.');
        }
      });
  }

  guardarGenero(): void {
    if (this.genero.nombre.trim() === '') {
      alert('Ingrese el nombre del género.');
      return;
    }

    this.http
      .post<any>(
        'http://localhost:8080/genero/guardar',
        this.genero
      )
      .subscribe({
        next: () => {
          alert('Género guardado exitosamente');
          this.limpiar();
          this.buscarGeneros();
        },
        error: (error) => {
          console.error('Error al guardar género:', error);
          alert('Error al guardar género.');
        }
      });
  }

  eliminarGenero(idGenero: number): void {
    this.http
      .delete(
        `http://localhost:8080/genero/eliminar/${idGenero}`
      )
      .subscribe({
        next: () => {
          alert('Género eliminado exitosamente');
          this.buscarGeneros();
        },
        error: (error) => {
          console.error('Error al eliminar género:', error);
          alert('Error al eliminar género.');
        }
      });
  }

  limpiar(): void {
    this.genero = {
      idGenero: null,
      nombre: ''
    };
  }
}