/**
 * DTO para requisicao de transferencia entre beneficios.
 *
 * Segue principios:
 * - SRP: Responsabilidade unica de representar dados de transferencia
 * - ISP: Interface segregada especifica para transfer
 */
export interface TransferRequestDTO {
  fromId: number;
  toId: number;
  amount: number;
}

