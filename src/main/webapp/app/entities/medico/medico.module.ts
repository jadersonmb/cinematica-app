import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CinematicaSharedModule } from 'app/shared/shared.module';
import { MedicoComponent } from './medico.component';
import { MedicoDetailComponent } from './medico-detail.component';
import { MedicoUpdateComponent } from './medico-update.component';
import { MedicoDeleteDialogComponent } from './medico-delete-dialog.component';
import { medicoRoute } from './medico.route';

@NgModule({
  imports: [CinematicaSharedModule, RouterModule.forChild(medicoRoute)],
  declarations: [MedicoComponent, MedicoDetailComponent, MedicoUpdateComponent, MedicoDeleteDialogComponent],
  entryComponents: [MedicoDeleteDialogComponent]
})
export class CinematicaMedicoModule {}
