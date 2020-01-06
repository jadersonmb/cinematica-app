import { Moment } from 'moment';
import { IHorarioDisponivel } from 'app/shared/model/horario-disponivel.model';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { IConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';

export interface IDataFalta {
  id?: number;
  dataFalta?: Moment;
  horarioDisponivels?: IHorarioDisponivel[];
  empresa?: IEmpresa;
  configuracaoAgenda?: IConfiguracaoAgenda;
}

export class DataFalta implements IDataFalta {
  constructor(
    public id?: number,
    public dataFalta?: Moment,
    public horarioDisponivels?: IHorarioDisponivel[],
    public empresa?: IEmpresa,
    public configuracaoAgenda?: IConfiguracaoAgenda
  ) {}
}
