import {
  IonApp,
  IonRouterOutlet,
  IonCard,
  IonCardContent,
  IonCardHeader,
  IonCardSubtitle,
  IonCardTitle,
  IonInput,
  IonItem,
  IonList,
  IonButton,
  IonContent,
} from '@ionic/angular';

import { Component, ChangeDetectorRef, inject } from '@angular/core';
import { personOutline, personAddOutline } from 'ionicons/icons';
import { addIcons } from 'ionicons';

import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    IonApp,
    IonRouterOutlet,
    IonCard,
    IonCardContent,
    IonCardHeader,
    IonCardSubtitle,
    IonCardTitle,
    IonInput,
    IonItem,
    IonList,
    IonButton,
    IonContent,
    
  ],
})
export class AppComponent {
  cliente: any = null;
  clienteLogin: any = {};
  clienteNuevo: any = {};
  crearNuevo: boolean = false;

  private readonly http = inject(HttpClient);
  private readonly cdr = inject(ChangeDetectorRef);

  constructor() {
    addIcons({ personOutline, personAddOutline });
  }

  

login() {
  if (this.clienteLogin.correo &&
      this.clienteLogin.contrasena) {

    this.http.post(
      'http://localhost:8080/cliente/login',
      this.clienteLogin
    ).subscribe({
      next: retorno => this.validar(retorno),
      error: () => alert('Error al iniciar sesión')
    });

  } else {
    alert('Complete la información');
  }
}




validar(cliente: any) {
  if (cliente?.idCliente) {

    const { contrasena, ...datosCliente } = cliente;

    this.cliente = datosCliente;

    localStorage.setItem(
      "cliente",
      JSON.stringify(datosCliente)
    );

    this.clienteLogin = {};
    this.cdr.detectChanges();

  } else {
    alert('Correo o contraseña inválidos');
  }
}


  crearNuevoBtn() {
    this.crearNuevo = true;
    this.clienteNuevo = {};
  }

  crear() {
    if (
      this.clienteNuevo.nit &&
      this.clienteNuevo.nombres &&
      this.clienteNuevo.apellidos &&
      this.clienteNuevo.correo &&
      this.clienteNuevo.contrasena
    ) {
      this.http
        .post('http://localhost:8080/cliente/guardar', this.clienteNuevo)
        .subscribe({
          next: (data) => this.mostrar(data),
          error: () => alert('Error al registrar el cliente'),
        });
    } else {
      alert('Complete los campos obligatorios');
    }
  }

  mostrar(data: any) {
    if (data?.idCliente) {
      alert('Cliente registrado correctamente');

      this.clienteNuevo = {};
      this.crearNuevo = false;

      this.cdr.detectChanges();
    } else {
      alert('Error al crear cliente');
    }
  }

  regresarLogin() {
    this.crearNuevo = false;
    this.clienteNuevo = {};
  }

  logout() {
    this.cliente = null;
    this.clienteLogin = {};
    this.crearNuevo = false;
    this.cdr.detectChanges();
  }
}
