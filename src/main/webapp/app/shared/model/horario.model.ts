export interface IHorario {
  id?: number;
  horarioInicio?: string;
  horarioFim?: string;
}

export class Horario implements IHorario {
  constructor(public id?: number, public horarioInicio?: string, public horarioFim?: string) {}
}
