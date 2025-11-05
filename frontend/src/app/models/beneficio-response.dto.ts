/**
 * DTO para resposta de Beneficio da API.
 * Representa a estrutura de dados retornada pelo backend.
 * 
 * Segue principios:
 * - SRP: Responsabilidade unica de representar dados
 * - ISP: Interface segregada especifica para response
 */
export interface BeneficioResponseDTO {
  id: number;
  nome: string;
  descricao: string;
  valor: number;
  ativo: boolean;
  version: number;
}
