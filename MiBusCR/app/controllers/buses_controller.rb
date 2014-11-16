class BusesController < ApplicationController
  before_action :set_bus, only: [:show]
  before_filter :restrict_access

  # GET /buses
  # GET /buses.json
  def index
    @buses = Bus.all

    render json: @buses.as_json(only: [:id, :placa])
  end

  # GET /buses/1
  # GET /buses/1.json
  def show
    render json: @bus.as_json(only: [:id, :placa],include: [gps:{only: [:id]}])
  end

  # POST /buses
  # POST /buses.json
  def create
    @bus = Bus.find_by_placa(params[:placa])
    if !params[:ruta].nil?
      @bus.ruta = Ruta.find(params[:ruta])
      @bus.save
    end
    render json: @bus.as_json(only: [:id, :placa],include: [ruta:{only: [:id]}])
  end

  # PATCH/PUT /buses/1
  # PATCH/PUT /buses/1.json
  def update
    head :unauthorized
  end

  # DELETE /buses/1
  # DELETE /buses/1.json
  def destroy
    head :unauthorized
  end

  private

    def set_bus
      begin  
        @bus = Bus.find(params[:id])
      rescue Exception => e  
        head 404
      end
    end
end
