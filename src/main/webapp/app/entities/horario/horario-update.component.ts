import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IHorario, Horario } from 'app/shared/model/horario.model';
import { HorarioService } from './horario.service';

@Component({
  selector: 'jhi-horario-update',
  templateUrl: './horario-update.component.html'
})
export class HorarioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    horarioInicio: [],
    horarioFim: []
  });

  constructor(protected horarioService: HorarioService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ horario }) => {
      this.updateForm(horario);
    });
  }

  updateForm(horario: IHorario): void {
    this.editForm.patchValue({
      id: horario.id,
      horarioInicio: horario.horarioInicio,
      horarioFim: horario.horarioFim
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const horario = this.createFromForm();
    if (horario.id !== undefined) {
      this.subscribeToSaveResponse(this.horarioService.update(horario));
    } else {
      this.subscribeToSaveResponse(this.horarioService.create(horario));
    }
  }

  private createFromForm(): IHorario {
    return {
      ...new Horario(),
      id: this.editForm.get(['id'])!.value,
      horarioInicio: this.editForm.get(['horarioInicio'])!.value,
      horarioFim: this.editForm.get(['horarioFim'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHorario>>): void {
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
