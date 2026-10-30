import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  imports: [],
  selector: 'app-asientos',
  styleUrl: './asientos.css',
  templateUrl: './asientos.html',
})
export class Asientos implements OnInit {
  idSala: number = 0;
  sala: any = null;
  asientos: any[][] = [];
  guardando: boolean = false;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.idSala = Number(this.route.snapshot.paramMap.get('idSala'));

    this.buscarSala();
  }

  buscarSala(): void {
    this.http.get<any[]>('http://localhost:8080/sala/buscar').subscribe({
      next: (data) => {
        this.sala = data.find((item) => item.idSala === this.idSala);
        if (this.sala) {
          this.generarAsientos();
        }

        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Error al obtener sala:', error);
        alert('Error al obtener los datos de la sala.');
      },
    });
  }

  generarAsientos(): void {
    this.asientos = [];

    for (let fila = 0; fila < this.sala.filas; fila++) {
      let asientosFila: any[] = [];

      for (let numero = 1; numero <= this.sala.asientosPorFila; numero++) {
        asientosFila.push({
          fila: fila + 1,
          numero: numero,
          nombre: String.fromCharCode(65 + fila) + numero,
        });
      }

      this.asientos.push(asientosFila);
    }
  }

  guardarAsientos(): void {
    if (!this.sala || this.guardando) {
      return;
    }

    this.guardando = true;

    const asientosGuardar = this.asientos.flat().map((asiento) => ({
      fila: String.fromCharCode(64 + asiento.fila),
      numero: asiento.numero,
      idSala: this.idSala,
    }));

    const total = asientosGuardar.length;
    let procesados = 0;
    let guardados = 0;

    if (total === 0) {
      this.guardando = false;
      return;
    }

  }
}
