import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  selector: 'app-anuncios',
  styleUrl: './anuncios.css',
  templateUrl: './anuncios.html',
})
export class Anuncios implements OnInit {
  NuevoAnuncio: FormGroup;
  AnunciosPublicados: any[] = [];

  // Controlar edición
  idAnuncioEditar: number | null = null;
  imagenActual = '';
  guardando = false;
  procesandoEstado: number | null = null;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) {
    this.NuevoAnuncio = this.fb.group({
      titulo: ['', [Validators.required]],
      descripcion: ['', Validators.required],
      fecha_publicacion: [''],
      imagen: [''],
      tipo: ['', Validators.required],
      tipo2: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.buscarAnuncios();
  }

  buscarAnuncios(): void {
    this.http.get<any[]>('http://localhost:8080/anuncio/buscar').subscribe({
      next: (data) => {
        this.AnunciosPublicados = data;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error al obtener anuncios:', error);
        alert('Error al obtener anuncios.');
        this.cdr.detectChanges();
      },
    });
  }

  // NUEVO: seleccionar anuncio para editar
  editarAnuncio(anuncio: any) {
    this.idAnuncioEditar = anuncio.idAnuncio;
    this.imagenActual = anuncio.imagen || '';

    this.NuevoAnuncio.patchValue({
      titulo: anuncio.titulo,
      descripcion: anuncio.descripcion,
      imagen: '',
      tipo: anuncio.tipo,
      tipo2: anuncio.tipo2
    });

    // Limpiar el selector de archivo anterior
    const input = document.getElementById('imagen') as HTMLInputElement | null;

    if (input) {
      input.value = '';
    }

    this.cdr.detectChanges();
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  }

  guardarAnuncio() {
    if (this.guardando) {
      return;
    }

    if (this.NuevoAnuncio.valid) {
      const datos: any = {
        titulo: this.NuevoAnuncio.value.titulo,
        descripcion: this.NuevoAnuncio.value.descripcion,
        imagen: this.NuevoAnuncio.value.imagen,
        tipo: this.NuevoAnuncio.value.tipo,
        tipo2: this.NuevoAnuncio.value.tipo2
      };

      // Si estamos editando, enviar el ID
      if (this.idAnuncioEditar !== null) {
        datos.idAnuncio = this.idAnuncioEditar;
      } else {
        datos.estado = true;
      }

      this.guardando = true;

      this.http.post<any>('http://localhost:8080/anuncio/guardar', datos).subscribe({
        next: (dataAnuncio) => {
          this.guardando = false;

          if (this.idAnuncioEditar !== null) {
            alert('Anuncio actualizado exitosamente.');
          } else {
            alert('Anuncio agregado exitosamente con el id: ' + dataAnuncio.idAnuncio);
          }

          this.limpiar();
          this.buscarAnuncios();
        },

        error: (error) => {
          this.guardando = false;
          console.error('Error al guardar anuncio:', error);
          alert('Error al guardar anuncio.');
          this.cdr.markForCheck();
        },
      });
    } else {
      this.NuevoAnuncio.markAllAsTouched();
      alert('Complete los datos');
    }
  }

  eliminarAnuncio(idAnuncio: number) {
    if (!confirm('¿Desea eliminar el anuncio ' + idAnuncio + '?')) {
      return;
    }

    this.http.delete(`http://localhost:8080/anuncio/eliminar/${idAnuncio}`).subscribe({
      next: () => {
        alert('Anuncio eliminado exitosamente.');

        if (this.idAnuncioEditar === idAnuncio) {
          this.limpiar();
        }

        this.buscarAnuncios();
      },
      error: (error) => {
        console.error('Error al eliminar anuncio:', error);
        alert('Error al eliminar anuncio.');
      },
    });
  }

  cambiarEstado(anuncio: any): void {
    if (this.procesandoEstado !== null) {
      return;
    }

    const accion = anuncio.estado ? 'desactivar' : 'activar';

    if (!confirm(`¿Desea ${accion} el anuncio "${anuncio.titulo}"?`)) {
      return;
    }

    this.procesandoEstado = anuncio.idAnuncio;
    this.cdr.detectChanges();

    this.http.put<any>(`http://localhost:8080/anuncio/estado/${anuncio.idAnuncio}`, {}).subscribe({
      next: (data) => {
        // Actualizar directamente la tabla
        this.AnunciosPublicados = this.AnunciosPublicados.map((item) =>
          item.idAnuncio === anuncio.idAnuncio ? { ...item, estado: data.estado } : item,
        );

        // Habilitar nuevamente el botón
        this.procesandoEstado = null;

        // Refrescar la vista inmediatamente
        this.cdr.detectChanges();

        alert('Estado actualizado correctamente.');
      },

      error: (error) => {
        console.error('Error al cambiar estado:', error);

        this.procesandoEstado = null;
        this.cdr.detectChanges();

        alert('No se pudo actualizar el estado.');
      },
    });
  }

  seleccionarImagen(event: Event) {
    const newfile = event.target as HTMLInputElement;

    if (newfile.files && newfile.files.length > 0) {
      const archivo = newfile.files[0];

      if (!archivo.type.startsWith('image/')) {
        alert('Seleccione una imagen válida.');
        newfile.value = '';
        return;
      }

      const reader = new FileReader();

      reader.onload = () => {
        const base64 = reader.result as string;

        this.NuevoAnuncio.patchValue({
          imagen: base64,
        });

        this.cdr.markForCheck();
      };

      reader.readAsDataURL(archivo);
    }
  }

  // NUEVO: mostrar imagen nueva o actual
  obtenerImagen(): string {
    return this.NuevoAnuncio.value.imagen || this.imagenActual;
  }

  // NUEVO: cancelar edición y limpiar formulario
  limpiar() {
    this.idAnuncioEditar = null;
    this.imagenActual = '';

    this.NuevoAnuncio.reset({
      titulo: '',
      descripcion: '',
      fecha_publicacion: '',
      imagen: '',
      tipo: '',
      tipo2: ''
    });

    const input = document.getElementById('imagen') as HTMLInputElement | null;

    if (input) {
      input.value = '';
    }

    this.cdr.markForCheck();
  }
}
