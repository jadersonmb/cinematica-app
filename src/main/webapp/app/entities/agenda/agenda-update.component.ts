import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAgenda, Agenda } from 'app/shared/model/agenda.model';
import { AgendaService } from './agenda.service';
import { IHorario } from 'app/shared/model/horario.model';
import { HorarioService } from 'app/entities/horario/horario.service';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { EmpresaService } from 'app/entities/empresa/empresa.service';
import { IPaciente } from 'app/shared/model/paciente.model';
import { PacienteService } from 'app/entities/paciente/paciente.service';
import { IEspecialidade } from 'app/shared/model/especialidade.model';
import { EspecialidadeService } from 'app/entities/especialidade/especialidade.service';

type SelectableEntity = IHorario | IEmpresa | IPaciente | IEspecialidade;

@Component({
  selector: 'jhi-agenda-update',
  templateUrl: './agenda-update.component.html'
})
export class AgendaUpdateComponent implements OnInit {
  isSaving = false;

  horarios: IHorario[] = [];

  empresas: IEmpresa[] = [];

  pacientes: IPaciente[] = [];

  especialidades: IEspecialidade[] = [];

  editForm = this.fb.group({
    id: [],
    dataInicio: [],
    dataFim: [],
    diaTodo: [],
    falta: [],
    cancelou: [],
    horario: [],
    empresa: [],
    paciente: [],
    funcionario: [],
    especialidade: []
  });

  constructor(
    protected agendaService: AgendaService,
    protected horarioService: HorarioService,
    protected empresaService: EmpresaService,
    protected pacienteService: PacienteService,
    protected especialidadeService: EspecialidadeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agenda }) => {
      this.updateForm(agenda);

      this.horarioService
        .query()
        .pipe(
          map((res: HttpResponse<IHorario[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IHorario[]) => (this.horarios = resBody));

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

      this.especialidadeService
        .query()
        .pipe(
          map((res: HttpResponse<IEspecialidade[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEspecialidade[]) => (this.especialidades = resBody));
    });
  }

  updateForm(agenda: IAgenda): void {
    this.editForm.patchValue({
      id: agenda.id,
      dataInicio: agenda.dataInicio != null ? agenda.dataInicio.format(DATE_TIME_FORMAT) : null,
      dataFim: agenda.dataFim != null ? agenda.dataFim.format(DATE_TIME_FORMAT) : null,
      diaTodo: agenda.diaTodo,
      falta: agenda.falta,
      cancelou: agenda.cancelou,
      horario: agenda.horario,
      empresa: agenda.empresa,
      paciente: agenda.paciente,
      funcionario: agenda.funcionario,
      especialidade: agenda.especialidade
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agenda = this.createFromForm();
    if (agenda.id !== undefined) {
      this.subscribeToSaveResponse(this.agendaService.update(agenda));
    } else {
      this.subscribeToSaveResponse(this.agendaService.create(agenda));
    }
  }

  private createFromForm(): IAgenda {
    return {
      ...new Agenda(),
      id: this.editForm.get(['id'])!.value,
      dataInicio:
        this.editForm.get(['dataInicio'])!.value != null ? moment(this.editForm.get(['dataInicio'])!.value, DATE_TIME_FORMAT) : undefined,
      dataFim: this.editForm.get(['dataFim'])!.value != null ? moment(this.editForm.get(['dataFim'])!.value, DATE_TIME_FORMAT) : undefined,
      diaTodo: this.editForm.get(['diaTodo'])!.value,
      falta: this.editForm.get(['falta'])!.value,
      cancelou: this.editForm.get(['cancelou'])!.value,
      horario: this.editForm.get(['horario'])!.value,
      empresa: this.editForm.get(['empresa'])!.value,
      paciente: this.editForm.get(['paciente'])!.value,
      funcionario: this.editForm.get(['funcionario'])!.value,
      especialidade: this.editForm.get(['especialidade'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgenda>>): void {
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
