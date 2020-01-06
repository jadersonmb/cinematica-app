export interface IProfissao {
  id?: number;
  descricao?: string;
}

export class Profissao implements IProfissao {
  constructor(public id?: number, public descricao?: string) {}
}
