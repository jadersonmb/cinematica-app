import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEspecialidade, Especialidade } from 'app/shared/model/especialidade.model';
import { EspecialidadeService } from './especialidade.service';

@Component({
  selector: 'jhi-especialidade-update',
  templateUrl: './especialidade-update.component.html'
})
export class EspecialidadeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descricao: []
  });

  constructor(protected especialidadeService: EspecialidadeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ especialidade }) => {
      this.updateForm(especialidade);
    });
  }

  updateForm(especialidade: IEspecialidade): void {
    this.editForm.patchValue({
      id: especialidade.id,
      descricao: especialidade.descricao
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const especialidade = this.createFromForm();
    if (especialidade.id !== undefined) {
      this.subscribeToSaveResponse(this.especialidadeService.update(especialidade));
    } else {
      this.subscribeToSaveResponse(this.especialidadeService.create(especialidade));
    }
  }

  private createFromForm(): IEspecialidade {
    return {
      ...new Especialidade(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEspecialidade>>): void {
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
