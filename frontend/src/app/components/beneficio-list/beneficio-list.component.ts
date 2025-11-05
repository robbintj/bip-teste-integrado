import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BeneficioService } from '../../services/beneficio.service';
import { ToastService } from '../../services/toast.service';
import { BeneficioResponseDTO } from '../../models/beneficio-response.dto';

/**
 * Componente standalone para listagem de beneficios.
 *
 * Utiliza Angular Signals para reatividade.
 * Segue principios:
 * - SRP: Responsabilidade unica de listar beneficios
 * - DIP: Depende de abstraco (services)
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './beneficio-list.component.html',
  styleUrls: ['./beneficio-list.component.scss']
})
export class BeneficioListComponent implements OnInit {
  beneficios = signal<BeneficioResponseDTO[]>([]);
  loading = signal(false);
  selectedBeneficio = signal<BeneficioResponseDTO | null>(null);

  constructor(
    private readonly beneficioService: BeneficioService,
    private readonly toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.loadBeneficios();
  }

  /**
   * Carrega lista de beneficios da API.
   */
  loadBeneficios(): void {
    this.loading.set(true);

    this.beneficioService.findAll().subscribe({
      next: (data) => {
        this.beneficios.set(data);
        this.loading.set(false);
      },
      error: (error) => {
        this.toastService.error('Erro', error.message);
        this.loading.set(false);
      }
    });
  }

  /**
   * Deleta beneficio.
   */
  deleteBeneficio(id: number): void {
    if (!confirm('Tem certeza que deseja excluir este beneficio?')) {
      return;
    }

    this.beneficioService.delete(id).subscribe({
      next: () => {
        this.toastService.success('Sucesso', 'Beneficio excluido com sucesso');
        this.loadBeneficios();
      },
      error: (error) => {
        this.toastService.error('Erro ao excluir', error.message);
      }
    });
  }

  /**
   * Formata valor para moeda brasileira.
   */
  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  }

  /**
   * Retorna classe CSS baseada no status.
   */
  getStatusClass(ativo: boolean): string {
    return ativo ? 'badge bg-success' : 'badge bg-secondary';
  }

  /**
   * Retorna texto do status.
   */
  getStatusText(ativo: boolean): string {
    return ativo ? 'Ativo' : 'Inativo';
  }
}

