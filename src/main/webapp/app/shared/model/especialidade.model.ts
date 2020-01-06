export interface IEspecialidade {
  id?: number;
  descricao?: string;
}

export class Especialidade implements IEspecialidade {
  constructor(public id?: number, public descricao?: string) {}
}
