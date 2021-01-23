import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Ruoli } from 'src/models/Ruoli';
import { ArticoliComponent } from './articoli/articoli.component';
import { ClientiComponent } from './clienti/clienti.component';
import { ErrorComponent } from './error/error.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { NewartComponent } from './newart/newart.component';
import { RouteGuardService } from './services/route-guard.service';
import { WelcomeComponent } from './welcome/welcome.component';

const routes: Routes = [
  {'path': '', 'component': LoginComponent },
  {'path': 'index', 'component': LoginComponent },
  {'path': 'login', 'component': LoginComponent },
  {'path': 'welcome/:userid', 'component': WelcomeComponent, canActivate: [RouteGuardService], data: { roles: [Ruoli.utente]} },
  {'path': 'articoli', 'component': ArticoliComponent, canActivate: [RouteGuardService], data: { roles: [Ruoli.utente]} },
  {'path': 'articoli/:filter', 'component': ArticoliComponent, canActivate: [RouteGuardService], data: { roles: [Ruoli.utente]} },
  {'path': 'newart/:codArt', 'component': NewartComponent, canActivate: [RouteGuardService], data: { roles: [Ruoli.amministratore]} },
  {'path': 'logout', 'component': LogoutComponent, canActivate: [RouteGuardService] },
  {'path':'clienti', 'component' : ClientiComponent},
  {path:'forbidden', component : ForbiddenComponent},
  {'path': '**', 'component': ErrorComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
