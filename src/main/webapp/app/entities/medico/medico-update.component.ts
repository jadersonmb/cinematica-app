import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IMedico, Medico } from 'app/shared/model/medico.model';
import { MedicoService } from './medico.service';
import { IEspecialidade } from 'app/shared/model/especialidade.model';
import { EspecialidadeService } from 'app/entities/especialidade/especialidade.service';

@Component({
  selector: 'jhi-medico-update',
  templateUrl: './medico-update.component.html'
})
export class MedicoUpdateComponent implements OnInit {
  isSaving = false;

  especialidades: IEspecialidade[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    telefone: [],
    email: [],
    especialidade: []
  });

  constructor(
    protected medicoService: MedicoService,
    protected especialidadeService: EspecialidadeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medico }) => {
      this.updateForm(medico);

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

  updateForm(medico: IMedico): void {
    this.editForm.patchValue({
      id: medico.id,
      nome: medico.nome,
      telefone: medico.telefone,
      email: medico.email,
      especialidade: medico.especialidade
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medico = this.createFromForm();
    if (medico.id !== undefined) {
      this.subscribeToSaveResponse(this.medicoService.update(medico));
    } else {
      this.subscribeToSaveResponse(this.medicoService.create(medico));
    }
  }

  private createFromForm(): IMedico {
    return {
      ...new Medico(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      telefone: this.editForm.get(['telefone'])!.value,
      email: this.editForm.get(['email'])!.value,
      especialidade: this.editForm.get(['especialidade'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedico>>): void {
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

  trackById(index: number, item: IEspecialidade): any {
    return item.id;
  }
}
