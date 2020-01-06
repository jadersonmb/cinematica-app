import { Moment } from 'moment';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { IEndereco } from 'app/shared/model/endereco.model';
import { IMedico } from 'app/shared/model/medico.model';
import { IProfissao } from 'app/shared/model/profissao.model';
import { Sexo } from 'app/shared/model/enumerations/sexo.model';

export interface IPaciente {
  id?: number;
  nome?: string;
  nomeCompleto?: string;
  criadoEm?: Moment;
  atualizadoEm?: Moment;
  cpf?: string;
  rg?: string;
  email?: string;
  telefoneCelular?: string;
  fotoUrl?: string;
  funcionario?: boolean;
  dataNascimento?: Moment;
  crefito?: string;
  telefone?: string;
  fotoUrlEndereco?: string;
  indicacao?: string;
  ativo?: boolean;
  sexo?: Sexo;
  empresa?: IEmpresa;
  endereco?: IEndereco;
  medico?: IMedico;
  profissao?: IProfissao;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public nome?: string,
    public nomeCompleto?: string,
    public criadoEm?: Moment,
    public atualizadoEm?: Moment,
    public cpf?: string,
    public rg?: string,
    public email?: string,
    public telefoneCelular?: string,
    public fotoUrl?: string,
    public funcionario?: boolean,
    public dataNascimento?: Moment,
    public crefito?: string,
    public telefone?: string,
    public fotoUrlEndereco?: string,
    public indicacao?: string,
    public ativo?: boolean,
    public sexo?: Sexo,
    public empresa?: IEmpresa,
    public endereco?: IEndereco,
    public medico?: IMedico,
    public profissao?: IProfissao
  ) {
    this.funcionario = this.funcionario || false;
    this.ativo = this.ativo || false;
  }
}
