import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IHorarioDisponivel, HorarioDisponivel } from 'app/shared/model/horario-disponivel.model';
import { HorarioDisponivelService } from './horario-disponivel.service';
import { IHorario } from 'app/shared/model/horario.model';
import { HorarioService } from 'app/entities/horario/horario.service';
import { IDataFalta } from 'app/shared/model/data-falta.model';
import { DataFaltaService } from 'app/entities/data-falta/data-falta.service';

type SelectableEntity = IHorario | IDataFalta;

@Component({
  selector: 'jhi-horario-disponivel-update',
  templateUrl: './horario-disponivel-update.component.html'
})
export class HorarioDisponivelUpdateComponent implements OnInit {
  isSaving = false;

  horarios: IHorario[] = [];

  datafaltas: IDataFalta[] = [];

  editForm = this.fb.group({
    id: [],
    horario: [],
    dataFalta: []
  });

  constructor(
    protected horarioDisponivelService: HorarioDisponivelService,
    protected horarioService: HorarioService,
    protected dataFaltaService: DataFaltaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ horarioDisponivel }) => {
      this.updateForm(horarioDisponivel);

      this.horarioService
        .query()
        .pipe(
          map((res: HttpResponse<IHorario[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IHorario[]) => (this.horarios = resBody));

      this.dataFaltaService
        .query()
        .pipe(
          map((res: HttpResponse<IDataFalta[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDataFalta[]) => (this.datafaltas = resBody));
    });
  }

  updateForm(horarioDisponivel: IHorarioDisponivel): void {
    this.editForm.patchValue({
      id: horarioDisponivel.id,
      horario: horarioDisponivel.horario,
      dataFalta: horarioDisponivel.dataFalta
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const horarioDisponivel = this.createFromForm();
    if (horarioDisponivel.id !== undefined) {
      this.subscribeToSaveResponse(this.horarioDisponivelService.update(horarioDisponivel));
    } else {
      this.subscribeToSaveResponse(this.horarioDisponivelService.create(horarioDisponivel));
    }
  }

  private createFromForm(): IHorarioDisponivel {
    return {
      ...new HorarioDisponivel(),
      id: this.editForm.get(['id'])!.value,
      horario: this.editForm.get(['horario'])!.value,
      dataFalta: this.editForm.get(['dataFalta'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHorarioDisponivel>>): void {
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
