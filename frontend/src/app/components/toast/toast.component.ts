import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService, ToastMessage } from '../../services/toast.service';

/**
 * Componente standalone para exibir notificacoes Toast.
 *
 * Segue principios:
 * - SRP: Responsabilidade unica de exibir toasts
 * - Standalone: Nao precisa de NgModule
 *
 * @author Robert R Serra
 */
@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container position-fixed top-0 end-0 p-3">
      @for (toast of toasts; track toast.id) {
        <div class="toast show fade-in"
             role="alert"
             [ngClass]="getToastClass(toast.type)">
          <div class="toast-header">
            <i [ngClass]="getIconClass(toast.type)" class="me-2"></i>
            <strong class="me-auto">{{ toast.title }}</strong>
            <button type="button"
                    class="btn-close"
                    (click)="close(toast.id)"
                    aria-label="Close"></button>
          </div>
          <div class="toast-body">
            {{ toast.message }}
          </div>
        </div>
      }
    </div>
  `,
  styles: [`
    .toast {
      margin-bottom: 0.5rem;
      min-width: 300px;
    }

    .toast-success {
      border-left: 4px solid #198754;
    }

    .toast-error {
      border-left: 4px solid #dc3545;
    }

    .toast-warning {
      border-left: 4px solid #ffc107;
    }

    .toast-info {
      border-left: 4px solid #0dcaf0;
    }
  `]
})
export class ToastComponent implements OnInit {
  toasts: ToastMessage[] = [];

  constructor(private readonly toastService: ToastService) {}

  ngOnInit(): void {
    this.toastService.toasts.subscribe(
      toasts => this.toasts = toasts
    );
  }

  close(id: number): void {
    this.toastService.remove(id);
  }

  getToastClass(type: ToastMessage['type']): string {
    return `toast-${type}`;
  }

  getIconClass(type: ToastMessage['type']): string {
    const icons = {
      success: 'bi bi-check-circle-fill text-success',
      error: 'bi bi-x-circle-fill text-danger',
      warning: 'bi bi-exclamation-triangle-fill text-warning',
      info: 'bi bi-info-circle-fill text-info'
    };
    return icons[type];
  }
}

