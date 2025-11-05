import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { BeneficioResponseDTO } from '../models/beneficio-response.dto';
import { BeneficioRequestDTO } from '../models/beneficio-request.dto';
import { TransferRequestDTO } from '../models/transfer-request.dto';
import { ErrorResponseDTO } from '../models/error-response.dto';

/**
 * Service para comunicacao com API de Beneficios.
 *
 * Segue principios SOLID:
 * - SRP: Responsabilidade unica de comunicacao com API
 * - DIP: Depende de abstraco (HttpClient)
 * - OCP: Aberto para extensao (metodos podem ser adicionados)
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private readonly API_URL = 'http://localhost:8080/api/v1/beneficios';

  constructor(private readonly http: HttpClient) {}

  /**
   * Lista todos os beneficios.
   */
  findAll(): Observable<BeneficioResponseDTO[]> {
    return this.http.get<BeneficioResponseDTO[]>(this.API_URL)
      .pipe(
        retry(2),
        catchError(this.handleError)
      );
  }

  /**
   * Busca beneficio por ID.
   */
  findById(id: number): Observable<BeneficioResponseDTO> {
    return this.http.get<BeneficioResponseDTO>(`${this.API_URL}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Cria novo beneficio.
   */
  create(beneficio: BeneficioRequestDTO): Observable<BeneficioResponseDTO> {
    return this.http.post<BeneficioResponseDTO>(this.API_URL, beneficio)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Atualiza beneficio existente.
   */
  update(id: number, beneficio: BeneficioRequestDTO): Observable<BeneficioResponseDTO> {
    return this.http.put<BeneficioResponseDTO>(`${this.API_URL}/${id}`, beneficio)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Remove beneficio por ID.
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Realiza transferencia entre beneficios.
   */
  transfer(transferData: TransferRequestDTO): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/transfer`, transferData)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Tratamento centralizado de erros HTTP.
   * Segue principio DRY (Don't Repeat Yourself).
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocorreu um erro desconhecido';

    if (error.error instanceof ErrorEvent) {
      // Erro do lado do cliente
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      // Erro do lado do servidor
      const errorResponse = error.error as ErrorResponseDTO;
      errorMessage = errorResponse?.message || `Erro ${error.status}: ${error.statusText}`;
    }

    console.error('Erro na requisicao:', error);
    return throwError(() => new Error(errorMessage));
  }
}

