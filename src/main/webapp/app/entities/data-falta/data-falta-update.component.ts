import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDataFalta, DataFalta } from 'app/shared/model/data-falta.model';
import { DataFaltaService } from './data-falta.service';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { EmpresaService } from 'app/entities/empresa/empresa.service';
import { IConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';
import { ConfiguracaoAgendaService } from 'app/entities/configuracao-agenda/configuracao-agenda.service';

type SelectableEntity = IEmpresa | IConfiguracaoAgenda;

@Component({
  selector: 'jhi-data-falta-update',
  templateUrl: './data-falta-update.component.html'
})
export class DataFaltaUpdateComponent implements OnInit {
  isSaving = false;

  empresas: IEmpresa[] = [];

  configuracaoagenda: IConfiguracaoAgenda[] = [];

  editForm = this.fb.group({
    id: [],
    dataFalta: [],
    empresa: [],
    configuracaoAgenda: []
  });

  constructor(
    protected dataFaltaService: DataFaltaService,
    protected empresaService: EmpresaService,
    protected configuracaoAgendaService: ConfiguracaoAgendaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dataFalta }) => {
      this.updateForm(dataFalta);

      this.empresaService
        .query()
        .pipe(
          map((res: HttpResponse<IEmpresa[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEmpresa[]) => (this.empresas = resBody));

      this.configuracaoAgendaService
        .query()
        .pipe(
          map((res: HttpResponse<IConfiguracaoAgenda[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IConfiguracaoAgenda[]) => (this.configuracaoagenda = resBody));
    });
  }

  updateForm(dataFalta: IDataFalta): void {
    this.editForm.patchValue({
      id: dataFalta.id,
      dataFalta: dataFalta.dataFalta != null ? dataFalta.dataFalta.format(DATE_TIME_FORMAT) : null,
      empresa: dataFalta.empresa,
      configuracaoAgenda: dataFalta.configuracaoAgenda
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dataFalta = this.createFromForm();
    if (dataFalta.id !== undefined) {
      this.subscribeToSaveResponse(this.dataFaltaService.update(dataFalta));
    } else {
      this.subscribeToSaveResponse(this.dataFaltaService.create(dataFalta));
    }
  }

  private createFromForm(): IDataFalta {
    return {
      ...new DataFalta(),
      id: this.editForm.get(['id'])!.value,
      dataFalta:
        this.editForm.get(['dataFalta'])!.value != null ? moment(this.editForm.get(['dataFalta'])!.value, DATE_TIME_FORMAT) : undefined,
      empresa: this.editForm.get(['empresa'])!.value,
      configuracaoAgenda: this.editForm.get(['configuracaoAgenda'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataFalta>>): void {
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
