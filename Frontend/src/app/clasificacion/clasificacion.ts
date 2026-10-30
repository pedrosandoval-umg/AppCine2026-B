import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-clasificacion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clasificacion.html',
  styleUrl: './clasificacion.css'
})
export class Clasificacion implements OnInit {

  clasificaciones: any[] = [];

  clasificacion = {
    idClasificacion: null,
    nombre: ''
  };

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.buscar();
  }

  buscar(): void {
    this.http
      .get<any[]>('http://localhost:8080/clasificacion/buscar')
      .subscribe({
        next: (data) => {
          this.clasificaciones = data;
          this.cdr.markForCheck();
        },
        error: (error) => {
          console.error('Error al obtener clasificaciones:', error);
          alert('Error al obtener clasificaciones.');
        }
      });
  }

  guardar(): void {
    this.http
      .post<any>(
        'http://localhost:8080/clasificacion/guardar',
        this.clasificacion
      )
      .subscribe({
        next: () => {
          alert('Clasificación guardada exitosamente');
          this.limpiar();
          this.buscar();
        },
        error: (error) => {
          console.error('Error al guardar clasificación:', error);
          alert('Error al guardar clasificación.');
        }
      });
  
  }

  eliminar(idClasificacion: number): void {
    this.http
      .delete(
        `http://localhost:8080/clasificacion/eliminar/${idClasificacion}`
      )
      .subscribe({
        next: () => {
          alert('Clasificación eliminada exitosamente');
          this.buscar();
        },
        error: (error) => {
          console.error('Error al eliminar clasificación:', error);
          alert('Error al eliminar clasificación.');
        }
      });
  }

  limpiar(): void {
    this.clasificacion = {
      idClasificacion: null,
      nombre: ''
    };
  }
}