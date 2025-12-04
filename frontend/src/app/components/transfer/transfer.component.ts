import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { BeneficioService } from '../../services/beneficio.service';
import { ToastService } from '../../services/toast.service';
import { BeneficioResponseDTO } from '../../models/beneficio-response.dto';
import { TransferRequestDTO } from '../../models/transfer-request.dto';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.scss']
})
export class TransferComponent implements OnInit {
  form!: FormGroup;
  beneficios = signal<BeneficioResponseDTO[]>([]);
  loading = signal(false);

  constructor(
    private readonly fb: FormBuilder,
    private readonly beneficioService: BeneficioService,
    private readonly toastService: ToastService,
    private readonly router: Router
  ) {
    this.createForm();
  }

  ngOnInit(): void {
    this.loadBeneficios();
  }

  private createForm(): void {
    this.form = this.fb.group({
      fromId: [null, Validators.required],
      toId: [null, Validators.required],
      amount: [0, [Validators.required, Validators.min(0.01)]]
    });
  }

  loadBeneficios(): void {
    this.beneficioService.findAll().subscribe({
      next: (data) => this.beneficios.set(data.filter(b => b.ativo)),
      error: (error) => this.toastService.error('Erro', error.message)
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.toastService.warning('Atencao', 'Preencha todos os campos');
      return;
    }

    const fromId = this.form.get('fromId')?.value;
    const toId = this.form.get('toId')?.value;

    if (fromId === toId) {
      this.toastService.warning('Atencao', 'Origem e destino devem ser diferentes');
      return;
    }

    this.loading.set(true);
    const transferData: TransferRequestDTO = this.form.value;

    this.beneficioService.transfer(transferData).subscribe({
      next: () => {
        this.toastService.success('Sucesso', 'Transferencia realizada com sucesso');
        this.router.navigate(['/beneficios']);
      },
      error: (error) => {
        this.toastService.error('Erro na transferencia', error.message);
        this.loading.set(false);
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/beneficios']);
  }

  getBeneficioNome(id: number): string {
    return this.beneficios().find(b => b.id === id)?.nome || '';
  }

  getBeneficioSaldo(id: number): number {
    return this.beneficios().find(b => b.id === id)?.valor || 0;
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  }

  getSaldoRestante(): number {
    const fromId = this.form.get('fromId')?.value;
    const amount = this.form.get('amount')?.value || 0;
    const saldo = this.getBeneficioSaldo(fromId);
    return saldo - amount;
  }
}
