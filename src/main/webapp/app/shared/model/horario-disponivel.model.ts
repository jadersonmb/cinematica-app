import { IHorario } from 'app/shared/model/horario.model';
import { IDataFalta } from 'app/shared/model/data-falta.model';

export interface IHorarioDisponivel {
  id?: number;
  horario?: IHorario;
  dataFalta?: IDataFalta;
}

export class HorarioDisponivel implements IHorarioDisponivel {
  constructor(public id?: number, public horario?: IHorario, public dataFalta?: IDataFalta) {}
}
