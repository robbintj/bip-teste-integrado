/**
 * DTO para requisicao de criacao/atualizacao de Beneficio.
 * Usado para enviar dados ao backend.
 *
 * Segue principios:
 * - SRP: Responsabilidade unica de representar dados de entrada
 * - ISP: Interface segregada especifica para request
 */
export interface BeneficioRequestDTO {
  nome: string;
  descricao: string;
  valor: number;
  ativo: boolean;
}

