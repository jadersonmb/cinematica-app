import { Moment } from 'moment';
import { IHorario } from 'app/shared/model/horario.model';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { IPaciente } from 'app/shared/model/paciente.model';
import { IEspecialidade } from 'app/shared/model/especialidade.model';

export interface IAgenda {
  id?: number;
  dataInicio?: Moment;
  dataFim?: Moment;
  diaTodo?: boolean;
  falta?: boolean;
  cancelou?: boolean;
  horario?: IHorario;
  empresa?: IEmpresa;
  paciente?: IPaciente;
  funcionario?: IPaciente;
  especialidade?: IEspecialidade;
}

export class Agenda implements IAgenda {
  constructor(
    public id?: number,
    public dataInicio?: Moment,
    public dataFim?: Moment,
    public diaTodo?: boolean,
    public falta?: boolean,
    public cancelou?: boolean,
    public horario?: IHorario,
    public empresa?: IEmpresa,
    public paciente?: IPaciente,
    public funcionario?: IPaciente,
    public especialidade?: IEspecialidade
  ) {
    this.diaTodo = this.diaTodo || false;
    this.falta = this.falta || false;
    this.cancelou = this.cancelou || false;
  }
}
