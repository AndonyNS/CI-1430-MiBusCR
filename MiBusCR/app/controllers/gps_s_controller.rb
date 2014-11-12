class GpsSController < ApplicationController
  before_action :set_gps, only: [:show]
  before_filter :restrict_access

  # GET /gps_s
  # GET /gps_s.json
  def index
    @gps_s = Gps.all

    render json: @gps_s.as_json(only: [:id, :id_gps,:latitud,:longitud])
  end

  # GET /gps_s/1
  # GET /gps_s/1.json
  def show
    render json: @gps.as_json(only: [:id, :id_gps,:latitud,:longitud])
  end

  # POST /gps_s
  # POST /gps_s.json
  def create
    @gps = Gps.find_by_id_gps(params[:id_gps])
    if @gps.nil?
      @gps = Gps.new()
      @gps.id_gps = params[:id_gps]
    end
    if params[:latitud] != ""
      @gps.latitud = params[:latitud]
    end
    if params[:longitud]
      @gps.longitud = params[:longitud]
    end
    @gps.save
    render json: @gps.as_json(only: [:id, :id_gps,:latitud,:longitud])
  end

  # PATCH/PUT /gps_s/1
  # PATCH/PUT /gps_s/1.json
  def update
    head :unauthorized
  end

  # DELETE /gps_s/1
  # DELETE /gps_s/1.json
  def destroy
    head :unauthorized
  end

  private
    # Never trust parameters from the scary internet, only allow the white list through.
    def gps_params
      params.permit(:id_gps)
    end

    def set_gps
      begin  
        @gps = Gps.find(params[:id])
      rescue Exception => e  
        head 404
      end
    end
end
