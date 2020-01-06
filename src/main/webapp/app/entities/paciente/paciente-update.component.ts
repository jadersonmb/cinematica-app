import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPaciente, Paciente } from 'app/shared/model/paciente.model';
import { PacienteService } from './paciente.service';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { EmpresaService } from 'app/entities/empresa/empresa.service';
import { IEndereco } from 'app/shared/model/endereco.model';
import { EnderecoService } from 'app/entities/endereco/endereco.service';
import { IMedico } from 'app/shared/model/medico.model';
import { MedicoService } from 'app/entities/medico/medico.service';
import { IProfissao } from 'app/shared/model/profissao.model';
import { ProfissaoService } from 'app/entities/profissao/profissao.service';

type SelectableEntity = IEmpresa | IEndereco | IMedico | IProfissao;

@Component({
  selector: 'jhi-paciente-update',
  templateUrl: './paciente-update.component.html'
})
export class PacienteUpdateComponent implements OnInit {
  isSaving = false;

  empresas: IEmpresa[] = [];

  enderecos: IEndereco[] = [];

  medicos: IMedico[] = [];

  profissaos: IProfissao[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    nomeCompleto: [],
    criadoEm: [],
    atualizadoEm: [],
    cpf: [],
    rg: [],
    email: [],
    telefoneCelular: [],
    fotoUrl: [],
    funcionario: [],
    dataNascimento: [],
    crefito: [],
    telefone: [],
    fotoUrlEndereco: [],
    indicacao: [],
    ativo: [],
    sexo: [],
    empresa: [],
    endereco: [],
    medico: [],
    profissao: []
  });

  constructor(
    protected pacienteService: PacienteService,
    protected empresaService: EmpresaService,
    protected enderecoService: EnderecoService,
    protected medicoService: MedicoService,
    protected profissaoService: ProfissaoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paciente }) => {
      this.updateForm(paciente);

      this.empresaService
        .query()
        .pipe(
          map((res: HttpResponse<IEmpresa[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEmpresa[]) => (this.empresas = resBody));

      this.enderecoService
        .query()
        .pipe(
          map((res: HttpResponse<IEndereco[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEndereco[]) => (this.enderecos = resBody));

      this.medicoService
        .query()
        .pipe(
          map((res: HttpResponse<IMedico[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IMedico[]) => (this.medicos = resBody));

      this.profissaoService
        .query()
        .pipe(
          map((res: HttpResponse<IProfissao[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProfissao[]) => (this.profissaos = resBody));
    });
  }

  updateForm(paciente: IPaciente): void {
    this.editForm.patchValue({
      id: paciente.id,
      nome: paciente.nome,
      nomeCompleto: paciente.nomeCompleto,
      criadoEm: paciente.criadoEm != null ? paciente.criadoEm.format(DATE_TIME_FORMAT) : null,
      atualizadoEm: paciente.atualizadoEm != null ? paciente.atualizadoEm.format(DATE_TIME_FORMAT) : null,
      cpf: paciente.cpf,
      rg: paciente.rg,
      email: paciente.email,
      telefoneCelular: paciente.telefoneCelular,
      fotoUrl: paciente.fotoUrl,
      funcionario: paciente.funcionario,
      dataNascimento: paciente.dataNascimento != null ? paciente.dataNascimento.format(DATE_TIME_FORMAT) : null,
      crefito: paciente.crefito,
      telefone: paciente.telefone,
      fotoUrlEndereco: paciente.fotoUrlEndereco,
      indicacao: paciente.indicacao,
      ativo: paciente.ativo,
      sexo: paciente.sexo,
      empresa: paciente.empresa,
      endereco: paciente.endereco,
      medico: paciente.medico,
      profissao: paciente.profissao
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paciente = this.createFromForm();
    if (paciente.id !== undefined) {
      this.subscribeToSaveResponse(this.pacienteService.update(paciente));
    } else {
      this.subscribeToSaveResponse(this.pacienteService.create(paciente));
    }
  }

  private createFromForm(): IPaciente {
    return {
      ...new Paciente(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      nomeCompleto: this.editForm.get(['nomeCompleto'])!.value,
      criadoEm:
        this.editForm.get(['criadoEm'])!.value != null ? moment(this.editForm.get(['criadoEm'])!.value, DATE_TIME_FORMAT) : undefined,
      atualizadoEm:
        this.editForm.get(['atualizadoEm'])!.value != null
          ? moment(this.editForm.get(['atualizadoEm'])!.value, DATE_TIME_FORMAT)
          : undefined,
      cpf: this.editForm.get(['cpf'])!.value,
      rg: this.editForm.get(['rg'])!.value,
      email: this.editForm.get(['email'])!.value,
      telefoneCelular: this.editForm.get(['telefoneCelular'])!.value,
      fotoUrl: this.editForm.get(['fotoUrl'])!.value,
      funcionario: this.editForm.get(['funcionario'])!.value,
      dataNascimento:
        this.editForm.get(['dataNascimento'])!.value != null
          ? moment(this.editForm.get(['dataNascimento'])!.value, DATE_TIME_FORMAT)
          : undefined,
      crefito: this.editForm.get(['crefito'])!.value,
      telefone: this.editForm.get(['telefone'])!.value,
      fotoUrlEndereco: this.editForm.get(['fotoUrlEndereco'])!.value,
      indicacao: this.editForm.get(['indicacao'])!.value,
      ativo: this.editForm.get(['ativo'])!.value,
      sexo: this.editForm.get(['sexo'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      endereco: this.editForm.get(['endereco'])!.value,
      medico: this.editForm.get(['medico'])!.value,
      profissao: this.editForm.get(['profissao'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaciente>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
