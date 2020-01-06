import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedico } from 'app/shared/model/medico.model';
import { MedicoService } from './medico.service';
import { MedicoDeleteDialogComponent } from './medico-delete-dialog.component';

@Component({
  selector: 'jhi-medico',
  templateUrl: './medico.component.html'
})
export class MedicoComponent implements OnInit, OnDestroy {
  medicos?: IMedico[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected medicoService: MedicoService,
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
      this.medicoService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IMedico[]>) => (this.medicos = res.body ? res.body : []));
      return;
    }
    this.medicoService.query().subscribe((res: HttpResponse<IMedico[]>) => {
      this.medicos = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMedicos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMedico): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMedicos(): void {
    this.eventSubscriber = this.eventManager.subscribe('medicoListModification', () => this.loadAll());
  }

  delete(medico: IMedico): void {
    const modalRef = this.modalService.open(MedicoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.medico = medico;
  }
}
