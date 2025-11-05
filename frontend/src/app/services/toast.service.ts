import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

/**
 * Interface para mensagem Toast.
 */
export interface ToastMessage {
  id: number;
  type: 'success' | 'error' | 'warning' | 'info';
  title: string;
  message: string;
  duration?: number;
}

/**
 * Service para gerenciamento de notificacoes Toast.
 *
 * Segue principios SOLID:
 * - SRP: Responsabilidade unica de gerenciar toasts
 * - OCP: Aberto para extensao (novos tipos de toast)
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private readonly toasts$ = new BehaviorSubject<ToastMessage[]>([]);
  private toastId = 0;

  /**
   * Observable para componentes se inscreverem.
   */
  get toasts(): Observable<ToastMessage[]> {
    return this.toasts$.asObservable();
  }

  /**
   * Exibe toast de sucesso.
   */
  success(title: string, message: string, duration = 3000): void {
    this.show('success', title, message, duration);
  }

  /**
   * Exibe toast de erro.
   */
  error(title: string, message: string, duration = 5000): void {
    this.show('error', title, message, duration);
  }

  /**
   * Exibe toast de aviso.
   */
  warning(title: string, message: string, duration = 4000): void {
    this.show('warning', title, message, duration);
  }

  /**
   * Exibe toast de informacao.
   */
  info(title: string, message: string, duration = 3000): void {
    this.show('info', title, message, duration);
  }

  /**
   * Remove toast por ID.
   */
  remove(id: number): void {
    const currentToasts = this.toasts$.value.filter(t => t.id !== id);
    this.toasts$.next(currentToasts);
  }

  /**
   * Metodo privado para adicionar toast.
   * Segue principio DRY.
   */
  private show(
    type: ToastMessage['type'],
    title: string,
    message: string,
    duration: number
  ): void {
    const toast: ToastMessage = {
      id: ++this.toastId,
      type,
      title,
      message,
      duration
    };

    const currentToasts = this.toasts$.value;
    this.toasts$.next([...currentToasts, toast]);

    // Auto-remover apos duracao
    if (duration > 0) {
      setTimeout(() => this.remove(toast.id), duration);
    }
  }

  /**
   * Limpa todos os toasts.
   */
  clear(): void {
    this.toasts$.next([]);
  }
}

