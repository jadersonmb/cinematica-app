import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IConfiguracaoAgenda, ConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';
import { ConfiguracaoAgendaService } from './configuracao-agenda.service';

@Component({
  selector: 'jhi-configuracao-agenda-update',
  templateUrl: './configuracao-agenda-update.component.html'
})
export class ConfiguracaoAgendaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    segunda: [],
    terca: [],
    quarta: [],
    quinta: [],
    sexta: [],
    sabado: [],
    domingo: []
  });

  constructor(
    protected configuracaoAgendaService: ConfiguracaoAgendaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configuracaoAgenda }) => {
      this.updateForm(configuracaoAgenda);
    });
  }

  updateForm(configuracaoAgenda: IConfiguracaoAgenda): void {
    this.editForm.patchValue({
      id: configuracaoAgenda.id,
      segunda: configuracaoAgenda.segunda,
      terca: configuracaoAgenda.terca,
      quarta: configuracaoAgenda.quarta,
      quinta: configuracaoAgenda.quinta,
      sexta: configuracaoAgenda.sexta,
      sabado: configuracaoAgenda.sabado,
      domingo: configuracaoAgenda.domingo
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configuracaoAgenda = this.createFromForm();
    if (configuracaoAgenda.id !== undefined) {
      this.subscribeToSaveResponse(this.configuracaoAgendaService.update(configuracaoAgenda));
    } else {
      this.subscribeToSaveResponse(this.configuracaoAgendaService.create(configuracaoAgenda));
    }
  }

  private createFromForm(): IConfiguracaoAgenda {
    return {
      ...new ConfiguracaoAgenda(),
      id: this.editForm.get(['id'])!.value,
      segunda: this.editForm.get(['segunda'])!.value,
      terca: this.editForm.get(['terca'])!.value,
      quarta: this.editForm.get(['quarta'])!.value,
      quinta: this.editForm.get(['quinta'])!.value,
      sexta: this.editForm.get(['sexta'])!.value,
      sabado: this.editForm.get(['sabado'])!.value,
      domingo: this.editForm.get(['domingo'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfiguracaoAgenda>>): void {
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
}
