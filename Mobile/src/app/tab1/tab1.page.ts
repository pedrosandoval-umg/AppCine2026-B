
import { Component, ChangeDetectorRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonCardSubtitle,
  IonCardContent
} from '@ionic/angular';

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss'],
  imports: [
    CommonModule,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardSubtitle,
    IonCardContent
  ]
})
export class Tab1Page {

  peliculas: any[] = [];

  private readonly http = inject(HttpClient);
  private readonly cdr = inject(ChangeDetectorRef);

  ionViewWillEnter() {
    this.buscarPeliculas();
  }

  buscarPeliculas() {
    this.http.get<any[]>(
      'http://localhost:8080/pelicula/activos'
    ).subscribe({
      next: data => {
        this.peliculas = data;
        this.cdr.detectChanges();
      },
      error: error => {
        console.error('Error al consultar películas:', error);
      }
    });
  }
}
