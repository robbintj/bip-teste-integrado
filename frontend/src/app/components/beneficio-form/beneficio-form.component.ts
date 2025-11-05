import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BeneficioService } from '../../services/beneficio.service';
import { ToastService } from '../../services/toast.service';
import { BeneficioRequestDTO } from '../../models/beneficio-request.dto';

/**
 * Componente standalone para formulario de beneficio (Create/Edit).
 *
 * Segue principios:
 * - SRP: Responsabilidade unica de gerenciar formulario
 * - DIP: Depende de abstraco (services)
 * - Reactive Forms para validacao
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Component({
  selector: 'app-beneficio-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl:'./beneficio-form.component.html',
  styleUrls: ['./beneficio-form.component.scss']
})
export class BeneficioFormComponent implements OnInit {
  form!: FormGroup;
  loading = signal(false);
  isEditMode = signal(false);
  beneficioId: number | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly beneficioService: BeneficioService,
    private readonly toastService: ToastService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    this.createForm();
  }

  ngOnInit(): void {
    // Verificar se e modo de edicao
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.beneficioId = +id;
      this.isEditMode.set(true);
      this.loadBeneficio();
    }
  }

  /**
   * Cria formulario reativo com validacoes.
   */
  private createForm(): void {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.maxLength(100)]],
      descricao: ['', [Validators.maxLength(255)]],
      valor: [0, [Validators.required, Validators.min(0)]],
      ativo: [true]
    });
  }

  /**
   * Carrega dados do beneficio para edicao.
   */
  private loadBeneficio(): void {
    if (!this.beneficioId) return;

    this.loading.set(true);

    this.beneficioService.findById(this.beneficioId).subscribe({
      next: (data) => {
        this.form.patchValue({
          nome: data.nome,
          descricao: data.descricao,
          valor: data.valor,
          ativo: data.ativo
        });
        this.loading.set(false);
      },
      error: (error) => {
        this.toastService.error('Erro', error.message);
        this.loading.set(false);
        this.router.navigate(['/beneficios']);
      }
    });
  }

  /**
   * Submete formulario (create ou update).
   */
  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.toastService.warning('Atencao', 'Preencha todos os campos obrigatorios');
      return;
    }

    this.loading.set(true);
    const beneficioData: BeneficioRequestDTO = this.form.value;

    const operation = this.isEditMode() && this.beneficioId
      ? this.beneficioService.update(this.beneficioId, beneficioData)
      : this.beneficioService.create(beneficioData);

    operation.subscribe({
      next: () => {
        const message = this.isEditMode()
          ? 'Beneficio atualizado com sucesso'
          : 'Beneficio criado com sucesso';

        this.toastService.success('Sucesso', message);
        this.router.navigate(['/beneficios']);
      },
      error: (error) => {
        this.toastService.error('Erro', error.message);
        this.loading.set(false);
      }
    });
  }

  /**
   * Cancela operacao e volta para lista.
   */
  onCancel(): void {
    this.router.navigate(['/beneficios']);
  }

  /**
   * Verifica se campo tem erro.
   */
  hasError(fieldName: string): boolean {
    const field = this.form.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  /**
   * Retorna mensagem de erro do campo.
   */
  getErrorMessage(fieldName: string): string {
    const field = this.form.get(fieldName);

    if (field?.hasError('required')) {
      return 'Campo obrigatorio';
    }

    if (field?.hasError('maxlength')) {
      const max = field.errors?.['maxlength'].requiredLength;
      return `Maximo de ${max} caracteres`;
    }

    if (field?.hasError('min')) {
      return 'Valor deve ser maior ou igual a zero';
    }

    return '';
  }

  /**
   * Retorna titulo do formulario.
   */
  getTitle(): string {
    return this.isEditMode() ? 'Editar Beneficio' : 'Novo Beneficio';
  }
}

