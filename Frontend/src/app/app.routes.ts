import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Login } from './login/login';
import { Anuncios } from './anuncios/anuncios';
import { Clasificacion } from './clasificacion/clasificacion';
import { Genero } from './genero/genero';
import { Sala } from './sala/sala';
import { Asientos } from './asientos/asientos';
import {Peliculas} from './peliculas/peliculas';
import { Funciones } from './funciones/funciones';
import { Reservas } from './reservas/reservas';

export const routes: Routes = [
{path: "", component: Login},
{path: "home", component: Home},
{path: "anuncios", component: Anuncios}, 
{path: "clasificacion", component: Clasificacion},
{path: "genero", component: Genero},
{path: "sala", component: Sala},
{path: "asientos/:idSala", component: Asientos},
{path: "peliculas", component: Peliculas},
{path: "funciones", component: Funciones},
{path: "reservas", component: Reservas}

];
