export interface IConfiguracaoAgenda {
  id?: number;
  segunda?: boolean;
  terca?: boolean;
  quarta?: boolean;
  quinta?: boolean;
  sexta?: boolean;
  sabado?: boolean;
  domingo?: boolean;
}

export class ConfiguracaoAgenda implements IConfiguracaoAgenda {
  constructor(
    public id?: number,
    public segunda?: boolean,
    public terca?: boolean,
    public quarta?: boolean,
    public quinta?: boolean,
    public sexta?: boolean,
    public sabado?: boolean,
    public domingo?: boolean
  ) {
    this.segunda = this.segunda || false;
    this.terca = this.terca || false;
    this.quarta = this.quarta || false;
    this.quinta = this.quinta || false;
    this.sexta = this.sexta || false;
    this.sabado = this.sabado || false;
    this.domingo = this.domingo || false;
  }
}
