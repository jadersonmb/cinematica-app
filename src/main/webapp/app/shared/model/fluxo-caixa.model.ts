import { Moment } from 'moment';
import { IEspecialidade } from 'app/shared/model/especialidade.model';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { IPaciente } from 'app/shared/model/paciente.model';
import { IFormaPagamento } from 'app/shared/model/forma-pagamento.model';
import { TipoLancamento } from 'app/shared/model/enumerations/tipo-lancamento.model';

export interface IFluxoCaixa {
  id?: number;
  dataLancamento?: Moment;
  descricao?: string;
  valor?: number;
  tipoLancamento?: TipoLancamento;
  numeroRecibo?: string;
  quantidadeParcela?: number;
  especialidade?: IEspecialidade;
  empresa?: IEmpresa;
  paciente?: IPaciente;
  formaPagamento?: IFormaPagamento;
}

export class FluxoCaixa implements IFluxoCaixa {
  constructor(
    public id?: number,
    public dataLancamento?: Moment,
    public descricao?: string,
    public valor?: number,
    public tipoLancamento?: TipoLancamento,
    public numeroRecibo?: string,
    public quantidadeParcela?: number,
    public especialidade?: IEspecialidade,
    public empresa?: IEmpresa,
    public paciente?: IPaciente,
    public formaPagamento?: IFormaPagamento
  ) {}
}
