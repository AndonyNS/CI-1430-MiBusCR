class RutaUsersController < ApplicationController
  before_filter :restrict_access

  # GET /ruta_users
  # GET /ruta_users.json
  def index
    @ruta_users = @current_user.ruta_user
    render json: @ruta_users.as_json(only: [:ruta_id])
  end

  # GET /ruta_users/1
  # GET /ruta_users/1.json
  def show
    head :unauthorized
  end

  # GET /ruta_users/new
  def new
    @ruta_user = RutaUser.new
  end

  # GET /ruta_users/1/edit
  def edit
  end

  # POST /ruta_users
  # POST /ruta_users.json
  def create
    if @current_user != nil
      if params[:id_ruta] != ""
        @ruta_user = @current_user.ruta_user.find_by(ruta_id: params[:id_ruta])
        if @ruta_user.nil?
          @ruta = Ruta.find(params[:id_ruta])
          @ruta_user = RutaUser.new(ruta:@ruta, user:@current_user)
        end
      end
      if @ruta_user.save
        render json: @ruta_user.as_json(only: [:ruta_id])
      else
        render json: @ruta_user.errors, status: :unprocessable_entity
      end
    end
  end

  # PATCH/PUT /ruta_users/1
  # PATCH/PUT /ruta_users/1.json
  def update
  end

  # DELETE /ruta_users/1
  # DELETE /ruta_users/1.json
  def destroy
    @ruta_user = @current_user.ruta_user.find_by(ruta_id: params[:id_ruta])
    @ruta_user.destroy
    head :no_content
  end
end
