import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CinematicaSharedModule } from 'app/shared/shared.module';
import { EspecialidadeComponent } from './especialidade.component';
import { EspecialidadeDetailComponent } from './especialidade-detail.component';
import { EspecialidadeUpdateComponent } from './especialidade-update.component';
import { EspecialidadeDeleteDialogComponent } from './especialidade-delete-dialog.component';
import { especialidadeRoute } from './especialidade.route';

@NgModule({
  imports: [CinematicaSharedModule, RouterModule.forChild(especialidadeRoute)],
  declarations: [EspecialidadeComponent, EspecialidadeDetailComponent, EspecialidadeUpdateComponent, EspecialidadeDeleteDialogComponent],
  entryComponents: [EspecialidadeDeleteDialogComponent]
})
export class CinematicaEspecialidadeModule {}
