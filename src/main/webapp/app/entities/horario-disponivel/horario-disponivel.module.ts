import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CinematicaSharedModule } from 'app/shared/shared.module';
import { HorarioDisponivelComponent } from './horario-disponivel.component';
import { HorarioDisponivelDetailComponent } from './horario-disponivel-detail.component';
import { HorarioDisponivelUpdateComponent } from './horario-disponivel-update.component';
import { HorarioDisponivelDeleteDialogComponent } from './horario-disponivel-delete-dialog.component';
import { horarioDisponivelRoute } from './horario-disponivel.route';

@NgModule({
  imports: [CinematicaSharedModule, RouterModule.forChild(horarioDisponivelRoute)],
  declarations: [
    HorarioDisponivelComponent,
    HorarioDisponivelDetailComponent,
    HorarioDisponivelUpdateComponent,
    HorarioDisponivelDeleteDialogComponent
  ],
  entryComponents: [HorarioDisponivelDeleteDialogComponent]
})
export class CinematicaHorarioDisponivelModule {}
