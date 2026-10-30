import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  selector: 'app-home',
  styleUrl: './home.css',
  templateUrl: './home.html',
})
export class Home implements OnInit {
  NuevoFormulario: FormGroup;
  UsuariosActivos: any[] = [];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {
    this.NuevoFormulario = this.fb.group({
      nombres: ['', [Validators.required]],
      apellidos: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', Validators.required],
      usuario: ['', Validators.required],
      contrasena: ['', Validators.required],
      idRol: ['', Validators.required],
      fotografia: [''],
    });
  }

  ngOnInit(): void {
    this.buscarUsuarios();
  }

  buscarUsuarios() {
    this.http.get<any[]>('http://localhost:8080/usuario/buscar').subscribe({
      next: (UsuariosActivos) => {
        this.UsuariosActivos = UsuariosActivos;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Error al obtener usuarios:', error);
        alert('Error al obtener usuarios.');
      },
    });
  }

  guardarUsuario() {
    if (this.NuevoFormulario.valid) {
      this.http
        .post<any>('http://localhost:8080/usuario/guardar', this.NuevoFormulario.value)
        .subscribe({
          next: (dataUsuario) => {
            if (dataUsuario?.idUsuario) {
              alert('Usuario agregado exitosamente con el id: ' + dataUsuario.idUsuario);
              this.NuevoFormulario.reset();
              this.buscarUsuarios();
            } else {
              alert('El servidor respondió, pero no devolvió un usuario válido.');
            }
          },

          error: (error) => {
            console.error('Error al crear usuario:', error);
            alert('Error al crear usuario.');
          },
        });
    } else {
      alert('Complete sus datos');
    }
  }

  eliminarUsuario(idUsuario: number) {
    this.http.delete(`http://localhost:8080/usuario/eliminar/${idUsuario}`).subscribe({
      next: () => {
        alert('Usuario eliminado exitosamente');
        this.buscarUsuarios();
      },
      error: (error) => {
        console.error('Error al eliminar usuario:', error);
        alert('Error al eliminar usuario.');
      },
    });
  }

  seleccionarFotografia(event: Event) {
    const newfile = event.target as HTMLInputElement;

    if (newfile.files && newfile.files.length > 0) {
      const archivo = newfile.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        const base64 = reader.result as string;

        this.NuevoFormulario.patchValue({
          fotografia: base64,
        });
      };
      reader.readAsDataURL(archivo);
    }
  }
}
