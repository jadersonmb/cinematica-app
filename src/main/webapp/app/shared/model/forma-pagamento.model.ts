export interface IFormaPagamento {
  id?: number;
  descricao?: string;
}

export class FormaPagamento implements IFormaPagamento {
  constructor(public id?: number, public descricao?: string) {}
}
