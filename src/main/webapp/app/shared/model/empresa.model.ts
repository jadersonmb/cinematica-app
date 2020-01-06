import { Moment } from 'moment';
import { IEndereco } from 'app/shared/model/endereco.model';

export interface IEmpresa {
  id?: number;
  nomeFantasia?: string;
  razaoSocial?: string;
  cnpj?: string;
  telefone?: string;
  email?: string;
  inscricaoEstadual?: string;
  inscricaoMunicipal?: string;
  website?: string;
  dataContratacao?: Moment;
  endereco?: IEndereco;
}

export class Empresa implements IEmpresa {
  constructor(
    public id?: number,
    public nomeFantasia?: string,
    public razaoSocial?: string,
    public cnpj?: string,
    public telefone?: string,
    public email?: string,
    public inscricaoEstadual?: string,
    public inscricaoMunicipal?: string,
    public website?: string,
    public dataContratacao?: Moment,
    public endereco?: IEndereco
  ) {}
}
