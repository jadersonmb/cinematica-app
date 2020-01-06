import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IFluxoCaixa, FluxoCaixa } from 'app/shared/model/fluxo-caixa.model';
import { FluxoCaixaService } from './fluxo-caixa.service';
import { IEspecialidade } from 'app/shared/model/especialidade.model';
import { EspecialidadeService } from 'app/entities/especialidade/especialidade.service';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { EmpresaService } from 'app/entities/empresa/empresa.service';
import { IPaciente } from 'app/shared/model/paciente.model';
import { PacienteService } from 'app/entities/paciente/paciente.service';
import { IFormaPagamento } from 'app/shared/model/forma-pagamento.model';
import { FormaPagamentoService } from 'app/entities/forma-pagamento/forma-pagamento.service';

type SelectableEntity = IEspecialidade | IEmpresa | IPaciente | IFormaPagamento;

@Component({
  selector: 'jhi-fluxo-caixa-update',
  templateUrl: './fluxo-caixa-update.component.html'
})
export class FluxoCaixaUpdateComponent implements OnInit {
  isSaving = false;

  especialidades: IEspecialidade[] = [];

  empresas: IEmpresa[] = [];

  pacientes: IPaciente[] = [];

  formapagamentos: IFormaPagamento[] = [];

  editForm = this.fb.group({
    id: [],
    dataLancamento: [],
    descricao: [],
    valor: [],
    tipoLancamento: [],
    numeroRecibo: [],
    quantidadeParcela: [],
    especialidade: [],
    empresa: [],
    paciente: [],
    formaPagamento: []
  });

  constructor(
    protected fluxoCaixaService: FluxoCaixaService,
    protected especialidadeService: EspecialidadeService,
    protected empresaService: EmpresaService,
    protected pacienteService: PacienteService,
    protected formaPagamentoService: FormaPagamentoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fluxoCaixa }) => {
      this.updateForm(fluxoCaixa);

      this.especialidadeService
        .query()
        .pipe(
          map((res: HttpResponse<IEspecialidade[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEspecialidade[]) => (this.especialidades = resBody));

      this.empresaService
        .query()
        .pipe(
          map((res: HttpResponse<IEmpresa[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEmpresa[]) => (this.empresas = resBody));

      this.pacienteService
        .query()
        .pipe(
          map((res: HttpResponse<IPaciente[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IPaciente[]) => (this.pacientes = resBody));

      this.formaPagamentoService
        .query()
        .pipe(
          map((res: HttpResponse<IFormaPagamento[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IFormaPagamento[]) => (this.formapagamentos = resBody));
    });
  }

  updateForm(fluxoCaixa: IFluxoCaixa): void {
    this.editForm.patchValue({
      id: fluxoCaixa.id,
      dataLancamento: fluxoCaixa.dataLancamento != null ? fluxoCaixa.dataLancamento.format(DATE_TIME_FORMAT) : null,
      descricao: fluxoCaixa.descricao,
      valor: fluxoCaixa.valor,
      tipoLancamento: fluxoCaixa.tipoLancamento,
      numeroRecibo: fluxoCaixa.numeroRecibo,
      quantidadeParcela: fluxoCaixa.quantidadeParcela,
      especialidade: fluxoCaixa.especialidade,
      empresa: fluxoCaixa.empresa,
      paciente: fluxoCaixa.paciente,
      formaPagamento: fluxoCaixa.formaPagamento
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fluxoCaixa = this.createFromForm();
    if (fluxoCaixa.id !== undefined) {
      this.subscribeToSaveResponse(this.fluxoCaixaService.update(fluxoCaixa));
    } else {
      this.subscribeToSaveResponse(this.fluxoCaixaService.create(fluxoCaixa));
    }
  }

  private createFromForm(): IFluxoCaixa {
    return {
      ...new FluxoCaixa(),
      id: this.editForm.get(['id'])!.value,
      dataLancamento:
        this.editForm.get(['dataLancamento'])!.value != null
          ? moment(this.editForm.get(['dataLancamento'])!.value, DATE_TIME_FORMAT)
          : undefined,
      descricao: this.editForm.get(['descricao'])!.value,
      valor: this.editForm.get(['valor'])!.value,
      tipoLancamento: this.editForm.get(['tipoLancamento'])!.value,
      numeroRecibo: this.editForm.get(['numeroRecibo'])!.value,
      quantidadeParcela: this.editForm.get(['quantidadeParcela'])!.value,
      especialidade: this.editForm.get(['especialidade'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      paciente: this.editForm.get(['paciente'])!.value,
      formaPagamento: this.editForm.get(['formaPagamento'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFluxoCaixa>>): void {
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
