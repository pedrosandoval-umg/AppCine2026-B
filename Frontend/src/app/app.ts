import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  imports: [RouterOutlet],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('proyectoB2026');

  usuario: any;

  constructor() {
    let u: any = localStorage.getItem("usuario");

    if (u) {
      this.usuario = JSON.parse(u);
    } else {
      if (window.location.pathname != "/") {
        location.href = "/";
      }
    }
  }

  cerrarSesion() {
    localStorage.removeItem("usuario");
    location.href = "/";
  }
}