class UsersController < ApplicationController
  before_action :set_user, only: [:show, :edit, :update, :destroy]
  before_filter :restrict_access

  # GET /users
  # GET /users.json
  def index
    @users = User.all

    render json: @users.as_json(only: [:id, :email, :nombre, :fechaNac, :ciudad])
  end

  # GET /users/1
  # GET /users/1.json
  def show
    if !@user.ruta.first.nil?
       render json: @user.as_json(only: [:id, :email, :nombre, :fechaNac, :ciudad], include: [ruta:{only: [:id]}])
    else
       render json: @user.as_json(only: [:id, :email, :nombre, :fechaNac, :ciudad])
    end
  end

  # POST /users
  # POST /users.json
  def create
    @user = User.new(user_params)
    if @user.save
      render json: @user.as_json(only: [:id, :email, :password, :nombre, :fechaNac, :ciudad, :token]), status: :created, location: @user
    else
      render json: @user.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /users/1
  # PATCH/PUT /users/1.json
  def update
    if @user.update(user_params)
      render json: @user.as_json(only: [:id, :email, :password, :nombre, :fechaNac, :ciudad])
    else
      render json: @user.errors, status: :unprocessable_entity
    end
  end

  # DELETE /users/1
  # DELETE /users/1.json
  def destroy
    @user.destroy

    head :no_content
  end

  private 
    # Never trust parameters from the scary internet, only allow the white list through. 
    def user_params
          params.permit(:email, :password, :nombre, :fechaNac, :ciudad)
    end

    # Use callbacks to share common setup or constraints between actions.
    def set_user
      begin  
       @user = User.find(params[:id])
      rescue Exception => e  
        head 404
      end
    end
end
