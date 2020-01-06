import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaciente } from 'app/shared/model/paciente.model';
import { PacienteService } from './paciente.service';
import { PacienteDeleteDialogComponent } from './paciente-delete-dialog.component';

@Component({
  selector: 'jhi-paciente',
  templateUrl: './paciente.component.html'
})
export class PacienteComponent implements OnInit, OnDestroy {
  pacientes?: IPaciente[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected pacienteService: PacienteService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.pacienteService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IPaciente[]>) => (this.pacientes = res.body ? res.body : []));
      return;
    }
    this.pacienteService.query().subscribe((res: HttpResponse<IPaciente[]>) => {
      this.pacientes = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPacientes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPaciente): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPacientes(): void {
    this.eventSubscriber = this.eventManager.subscribe('pacienteListModification', () => this.loadAll());
  }

  delete(paciente: IPaciente): void {
    const modalRef = this.modalService.open(PacienteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paciente = paciente;
  }
}
