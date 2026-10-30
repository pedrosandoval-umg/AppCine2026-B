import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  selector: 'app-login',
  styleUrl: './login.css',
  templateUrl: './login.html',
})
export class Login {
  formulario: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
  ) {
    this.formulario = this.fb.group({
      usuario: ['', [Validators.required]],
      contrasena: ['', Validators.required],
    });
  }

  login() {
    if (this.formulario.valid) {
      this.http
        .post('http://localhost:8080/usuario/login', this.formulario.value)
        .subscribe((retorno) => this.validarLogin(retorno));
    } else {
      alert('Complete sus datos');
    }
  }

  validarLogin(usuario: any) {
    if (usuario?.usuario) {
      localStorage.setItem(
        'usuario',
        JSON.stringify({
          idUsuario: usuario.idUsuario,
          usuario: usuario.usuario,
          nombres: usuario.nombres,
        }),
      );

      location.href = '/home';
    } else {
      alert('Usuario o contraseña incorrectos');
    }
  }
}
