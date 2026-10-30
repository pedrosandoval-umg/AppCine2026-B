
import { Component, ChangeDetectorRef, inject, OnInit } from '@angular/core';
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
  IonCardContent,
  IonCardSubtitle
} from '@ionic/angular';

@Component({
  selector: 'app-tab3',
  templateUrl: 'tab3.page.html',
  styleUrls: ['tab3.page.scss'],
  imports: [
    CommonModule,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonCardSubtitle
  ]
})
export class Tab3Page  {

  anuncios: any[] = [];

  private readonly http = inject(HttpClient);
  private readonly cdr = inject(ChangeDetectorRef);

ionViewWillEnter() {
  this.buscarAnuncios();
  }

  buscarAnuncios() {
    this.http.get<any[]>(
      'http://localhost:8080/anuncio/activos'
    ).subscribe({
      next: data => {
        this.anuncios = data;
        this.cdr.detectChanges();
      },
      error: error => {
        console.error('Error al consultar anuncios:', error);
      }
    });
  }
}
