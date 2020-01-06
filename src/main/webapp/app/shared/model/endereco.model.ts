export interface IEndereco {
  id?: number;
  cep?: string;
  nomeEndereco?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  numero?: number;
  complemento?: string;
}

export class Endereco implements IEndereco {
  constructor(
    public id?: number,
    public cep?: string,
    public nomeEndereco?: string,
    public bairro?: string,
    public cidade?: string,
    public estado?: string,
    public numero?: number,
    public complemento?: string
  ) {}
}
