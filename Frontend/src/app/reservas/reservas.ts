
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reservas.html',
  styleUrl: './reservas.css'
})
export class Reservas implements OnInit {

  // Listas
  funciones: any[] = [];
  peliculas: any[] = [];
  salas: any[] = [];
  reservas: any[] = [];
  asientos: any[] = [];

  // Búsqueda de clientes
  criterioBusqueda = 'nit';
  valorBusqueda = '';
  filtroNombreCF = '';

  clientesEncontrados: any[] = [];
  cliente: any = null;

  buscandoCliente = false;
  busquedaRealizada = false;
  private numeroBusqueda = 0;

  // Registro rápido de clientes
  mostrarRegistroCliente = false;
  guardandoCliente = false;

  nuevoCliente = {
    nit: '',
    nombres: '',
    apellidos: '',
    telefono: '',
    correo: ''
  };

  // Reservas
  seleccionManual = false;
  procesando = false;

  reserva = {
    idCliente: null as number | null,
    idFuncion: null as number | null,
    cantidadEntradas: 1,
    asientos: [] as number[]
  };

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.buscarFunciones();
    this.buscarPeliculas();
    this.buscarSalas();
    this.buscarReservas();
  }

  // CONSULTAR FUNCIONES
  buscarFunciones(): void {
    this.http.get<any[]>(
      'http://localhost:8080/funcion/buscar'
    ).subscribe({
      next: data => {
        this.funciones = data;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Error al buscar funciones:', error);
      }
    });
  }

  // CONSULTAR PELÍCULAS
  buscarPeliculas(): void {
    this.http.get<any[]>(
      'http://localhost:8080/pelicula/buscar'
    ).subscribe({
      next: data => {
        this.peliculas = data;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Error al buscar películas:', error);
      }
    });
  }

  // CONSULTAR SALAS
  buscarSalas(): void {
    this.http.get<any[]>(
      'http://localhost:8080/sala/buscar'
    ).subscribe({
      next: data => {
        this.salas = data;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Error al buscar salas:', error);
      }
    });
  }

  // CONSULTAR RESERVAS REGISTRADAS
  buscarReservas(): void {
    this.http.get<any[]>(
      'http://localhost:8080/reserva/buscar/detalle'
    ).subscribe({
      next: data => {
        this.reservas = data;
        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Error al buscar reservas:', error);
      }
    });
  }

  // BUSCAR CLIENTE
  buscarCliente(): void {

    const valor = this.valorBusqueda.trim();

    if (valor === '') {
      alert('Ingrese un valor de búsqueda.');
      return;
    }

    this.limpiarBusqueda();
    this.mostrarRegistroCliente = false;

    const criterio = this.criterioBusqueda;
    const busquedaActual = ++this.numeroBusqueda;

    const parametros =
      `criterio=${encodeURIComponent(criterio)}` +
      `&valor=${encodeURIComponent(valor)}`;

    this.buscandoCliente = true;

    this.http.get<any[]>(
      `http://localhost:8080/cliente/buscar/filtro?${parametros}`
    ).subscribe({
      next: data => {

        if (busquedaActual !== this.numeroBusqueda) {
          return;
        }

        this.clientesEncontrados = data;
        this.busquedaRealizada = true;
        this.buscandoCliente = false;

        this.cdr.markForCheck();
      },
      error: error => {

        if (busquedaActual !== this.numeroBusqueda) {
          return;
        }

        console.error('Error al buscar clientes:', error);

        this.buscandoCliente = false;
        alert('No se pudo realizar la búsqueda.');

        this.cdr.markForCheck();
      }
    });
  }

  // SELECCIONAR CLIENTE EXISTENTE
  seleccionarCliente(item: any): void {

    this.cliente = item;
    this.reserva.idCliente = item.idCliente;

    this.mostrarRegistroCliente = false;
    this.busquedaRealizada = false;

    this.cdr.markForCheck();
  }

  // LIMPIAR BÚSQUEDA
  limpiarBusqueda(): void {

    this.numeroBusqueda++;

    this.cliente = null;
    this.reserva.idCliente = null;

    this.clientesEncontrados = [];
    this.busquedaRealizada = false;
    this.buscandoCliente = false;
    this.filtroNombreCF = '';

    this.cdr.markForCheck();
  }

  // FILTRAR RESULTADOS CF POR NOMBRE
  clientesFiltrados(): any[] {

    if (
      this.criterioBusqueda !== 'nit' ||
      this.valorBusqueda.trim().toUpperCase() !== 'CF'
    ) {
      return this.clientesEncontrados;
    }

    const nombre = this.filtroNombreCF.trim().toLowerCase();

    if (nombre === '') {
      return this.clientesEncontrados;
    }

    return this.clientesEncontrados.filter(item => {

      const nombreCompleto =
        `${item.nombres || ''} ${item.apellidos || ''}`
        .toLowerCase();

      return nombreCompleto.includes(nombre);
    });
  }

  // ABRIR FORMULARIO DE NUEVO CLIENTE
  abrirRegistroCliente(): void {

    const nitIngresado =
      this.criterioBusqueda === 'nit'
        ? this.valorBusqueda.trim().toUpperCase()
        : '';

    this.limpiarBusqueda();

    this.nuevoCliente = {
      nit: nitIngresado,
      nombres: '',
      apellidos: '',
      telefono: '',
      correo: ''
    };

    this.mostrarRegistroCliente = true;
    this.cdr.markForCheck();
  }

  // CERRAR FORMULARIO DE CLIENTE
  cerrarRegistroCliente(): void {

    if (this.guardandoCliente) {
      return;
    }

    this.mostrarRegistroCliente = false;
    this.cdr.markForCheck();
  }

  // REGISTRAR NUEVO CLIENTE
  guardarCliente(): void {

    if (this.guardandoCliente) {
      return;
    }

    if (
      this.nuevoCliente.nit.trim() === '' ||
      this.nuevoCliente.nombres.trim() === '' ||
      this.nuevoCliente.apellidos.trim() === ''
    ) {
      alert('Ingrese NIT, nombres y apellidos.');
      return;
    }

    const datos = {
      nit: this.nuevoCliente.nit.trim().toUpperCase(),
      nombres: this.nuevoCliente.nombres.trim(),
      apellidos: this.nuevoCliente.apellidos.trim(),
      telefono: this.nuevoCliente.telefono.trim(),
      correo: this.nuevoCliente.correo.trim()
    };

    this.guardandoCliente = true;

    this.http.post<any>(
      'http://localhost:8080/cliente/registrar-rapido',
      datos
    ).subscribe({
      next: data => {

        this.guardandoCliente = false;

        // Seleccionar automáticamente el cliente nuevo
        this.seleccionarCliente(data);

        this.criterioBusqueda = 'nit';
        this.valorBusqueda = data.nit;

        this.nuevoCliente = {
          nit: '',
          nombres: '',
          apellidos: '',
          telefono: '',
          correo: ''
        };

        alert('Cliente registrado correctamente.');
        this.cdr.markForCheck();
      },
      error: error => {

        this.guardandoCliente = false;

        console.error('Error al registrar cliente:', error);

        alert(
          error.status === 409
            ? 'Ya existe un cliente con ese NIT.'
            : 'No se pudo registrar el cliente.'
        );

        this.cdr.markForCheck();
      }
    });
  }

  // CAMBIAR FUNCIÓN
  cambiarFuncion(): void {

    this.asientos = [];
    this.reserva.asientos = [];

    if (this.reserva.idFuncion !== null) {
      this.buscarAsientos();
    }

    this.cdr.markForCheck();
  }

  // CONSULTAR DISPONIBILIDAD
  buscarAsientos(): void {

    const idFuncion = this.reserva.idFuncion;

    if (idFuncion === null) {
      this.asientos = [];
      this.cdr.markForCheck();
      return;
    }

    this.http.get<any[]>(
      `http://localhost:8080/asiento/disponibles/${idFuncion}`
    ).subscribe({
      next: data => {

        // Evitar mostrar asientos de otra función
        if (this.reserva.idFuncion !== idFuncion) {
          return;
        }

        this.asientos = data;

        const disponibles = new Set(
          data
            .filter(a => a.disponible)
            .map(a => a.idAsiento)
        );

        this.reserva.asientos = this.reserva.asientos
          .filter(id => disponibles.has(id));

        this.cdr.markForCheck();
      },
      error: error => {
        console.error('Error al consultar asientos:', error);
        alert('No se pudieron consultar los asientos.');
        this.cdr.markForCheck();
      }
    });
  }

  // SELECCIONAR ASIENTO
  seleccionarAsiento(asiento: any): void {

    if (!asiento.disponible) {
      return;
    }

    const id = asiento.idAsiento;
    const indice = this.reserva.asientos.indexOf(id);

    if (indice >= 0) {

      this.reserva.asientos.splice(indice, 1);

    } else {

      if (
        this.reserva.asientos.length >=
        this.reserva.cantidadEntradas
      ) {
        alert('Ya seleccionó la cantidad de asientos indicada.');
        return;
      }

      this.reserva.asientos.push(id);
    }

    this.cdr.markForCheck();
  }

  // CAMBIAR CANTIDAD DE ENTRADAS
  cambiarCantidad(): void {
    this.reserva.asientos = [];
  }

  // CAMBIAR MODO AUTOMÁTICO / MANUAL
  cambiarModo(): void {

    this.reserva.asientos = [];

    if (this.reserva.idFuncion !== null) {
      this.buscarAsientos();
    }
  }

  // CONTAR ASIENTOS DISPONIBLES
  disponibles(): number {
    return this.asientos.filter(
      a => a.disponible
    ).length;
  }

  // AGRUPAR ASIENTOS POR FILA
  agruparAsientos(): { fila: string, asientos: any[] }[] {

    const filas: { fila: string, asientos: any[] }[] = [];

    for (const asiento of this.asientos) {

      let grupo = filas.find(
        item => item.fila === asiento.fila
      );

      if (!grupo) {

        grupo = {
          fila: asiento.fila,
          asientos: []
        };

        filas.push(grupo);
      }

      grupo.asientos.push(asiento);
    }

    filas.sort((a, b) =>
      a.fila.localeCompare(b.fila)
    );

    for (const grupo of filas) {
      grupo.asientos.sort(
        (a, b) => a.numero - b.numero
      );
    }

    return filas;
  }

  // OBTENER NOMBRE DE PELÍCULA
  nombrePelicula(idPelicula: number): string {

    const pelicula = this.peliculas.find(
      p => p.idPelicula === idPelicula
    );

    return pelicula ? pelicula.titulo : '';
  }

  // OBTENER NOMBRE DE SALA
  nombreSala(idSala: number): string {

    const sala = this.salas.find(
      s => s.idSala === idSala
    );

    return sala ? sala.nombre : '';
  }

  // DESCRIPCIÓN DE LA FUNCIÓN
  descripcionFuncion(idFuncion: number): string {

    const funcion = this.funciones.find(
      f => f.idFuncion === idFuncion
    );

    if (!funcion) {
      return `Función ${idFuncion}`;
    }

    return `${this.nombrePelicula(funcion.idPelicula)}
      - ${this.nombreSala(funcion.idSala)}
      - ${funcion.fecha} ${funcion.hora}`;
  }

  // CALCULAR TOTAL ESTIMADO
  total(): number {

    const funcion = this.funciones.find(
      f => f.idFuncion === this.reserva.idFuncion
    );

    return funcion
      ? Number(funcion.precioEntrada) *
        this.reserva.cantidadEntradas
      : 0;
  }

  // REGISTRAR RESERVA
  guardarReserva(): void {

    if (this.procesando || this.guardandoCliente) {
      return;
    }

    if (
      this.reserva.idCliente === null ||
      this.reserva.idFuncion === null ||
      !Number.isInteger(this.reserva.cantidadEntradas) ||
      this.reserva.cantidadEntradas <= 0
    ) {
      alert(
        'Seleccione una función, un cliente y una cantidad válida.'
      );
      return;
    }

    if (
      this.seleccionManual &&
      this.reserva.asientos.length !==
      this.reserva.cantidadEntradas
    ) {
      alert('Seleccione la cantidad exacta de asientos.');
      return;
    }

    const datos: any = {
      idCliente: this.reserva.idCliente,
      idFuncion: this.reserva.idFuncion,
      cantidadEntradas: this.reserva.cantidadEntradas
    };

    if (this.seleccionManual) {
      datos.asientos = [...this.reserva.asientos];
    }

    this.procesando = true;

    this.http.post<any>(
      'http://localhost:8080/reserva/registrar',
      datos
    ).subscribe({
      next: data => {

        this.procesando = false;

        alert(
          `Reserva ${data.idReserva} registrada. ` +
          `Total: Q${Number(data.totalAPagar).toFixed(2)}`
        );

        this.limpiar();
        this.buscarReservas();
      },
      error: error => {

        this.procesando = false;

        console.error('Error al registrar reserva:', error);

        alert(
          error.status === 409
            ? 'No hay disponibilidad para los asientos solicitados.'
            : 'No se pudo registrar la reserva.'
        );

        this.buscarAsientos();
        this.cdr.markForCheck();
      }
    });
  }

  // CANCELAR RESERVA
  cancelarReserva(item: any): void {

    if (!item.estado) {
      return;
    }

    if (!confirm(
      `¿Desea cancelar la reserva ${item.idReserva}?`
    )) {
      return;
    }

    this.http.put<any>(
      `http://localhost:8080/reserva/cancelar/${item.idReserva}`,
      {}
    ).subscribe({
      next: () => {

        alert('Reserva cancelada correctamente.');

        this.buscarReservas();

        if (this.reserva.idFuncion !== null) {
          this.buscarAsientos();
        }
      },
      error: error => {
        console.error('Error al cancelar reserva:', error);
        alert('No se pudo cancelar la reserva.');
      }
    });
  }

  // LIMPIAR FORMULARIO COMPLETO
  limpiar(): void {

    this.numeroBusqueda++;

    this.criterioBusqueda = 'nit';
    this.valorBusqueda = '';
    this.filtroNombreCF = '';

    this.clientesEncontrados = [];
    this.cliente = null;
    this.buscandoCliente = false;
    this.busquedaRealizada = false;

    this.mostrarRegistroCliente = false;
    this.guardandoCliente = false;

    this.nuevoCliente = {
      nit: '',
      nombres: '',
      apellidos: '',
      telefono: '',
      correo: ''
    };

    this.seleccionManual = false;
    this.asientos = [];

    this.reserva = {
      idCliente: null,
      idFuncion: null,
      cantidadEntradas: 1,
      asientos: []
    };

    this.cdr.markForCheck();
  }
}
