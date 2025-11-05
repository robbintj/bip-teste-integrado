import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastComponent } from './components/toast/toast.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ToastComponent],
  template: `
    <nav class="navbar navbar-dark bg-primary shadow-sm">
      <div class="container">
        <a class="navbar-brand" routerLink="/">
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

