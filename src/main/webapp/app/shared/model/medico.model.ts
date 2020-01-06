import { IEspecialidade } from 'app/shared/model/especialidade.model';

export interface IMedico {
  id?: number;
  nome?: string;
  telefone?: string;
  email?: string;
  especialidade?: IEspecialidade;
}

export class Medico implements IMedico {
  constructor(
    public id?: number,
    public nome?: string,
    public telefone?: string,
    public email?: string,
    public especialidade?: IEspecialidade
  ) {}
}
