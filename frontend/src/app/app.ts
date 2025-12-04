import { Component, ApplicationConfig } from '@angular/core';
import { RouterOutlet, provideRouter, Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { ToastComponent } from './components/toast/toast.component';
import { BeneficioListComponent } from './components/beneficio-list/beneficio-list.component';
import { BeneficioFormComponent } from './components/beneficio-form/beneficio-form.component';
import { TransferComponent } from './components/transfer/transfer.component';

const routes: Routes = [
  { path: '', redirectTo: '/beneficios', pathMatch: 'full' },
  { path: 'beneficios', component: BeneficioListComponent },
  { path: 'beneficios/novo', component: BeneficioFormComponent },
  { path: 'beneficios/:id/editar', component: BeneficioFormComponent },
  { path: 'beneficios/:id/transferir', component: TransferComponent },
  { path: 'transferencia', component: TransferComponent }
];

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient()
  ]
};

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastComponent],
  template: `
    <nav class="navbar navbar-dark bg-primary shadow-sm">
      <div class="container">
        <a class="navbar-brand" href="/">
          <i class="bi bi-gift me-2"></i>
          Sistema de Beneficios
        </a>
      </div>
    </nav>

    <router-outlet></router-outlet>
    <app-toast></app-toast>
  `,
  styles: [`
    .navbar {
      margin-bottom: 0;
    }
  `]
})
export class AppComponent {
  title = 'Sistema de Beneficios';
}
